package com.example.crud.application.app_service.board.common;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.crud.application.dto.board.BoardSearchPagingResponseDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardSearchPagingService {

    private final ElasticsearchClient elasticsearchClient;

    public Page<BoardSearchPagingResponseDto> searchBoards(String keyword, String category, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, size);

        BoolQuery.Builder queryBuilder = new BoolQuery.Builder();

        if (keyword != null && !keyword.isEmpty()) {
            queryBuilder.must(MatchQuery.of(m -> m.field("title").query(keyword))._toQuery());
        }

        if (category != null && !category.isEmpty()) {
            queryBuilder.filter(TermQuery.of(t -> t.field("category").value(category.toUpperCase()))._toQuery());
        }

        Query query = queryBuilder.build()._toQuery();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("boards")
                .query(query)
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize())
                .sort(s -> s.field(f -> f.field("createdAt").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                .build();

        SearchResponse<BoardSearchPagingResponseDto> searchResponse = elasticsearchClient.search(searchRequest, BoardSearchPagingResponseDto.class);

        List<BoardSearchPagingResponseDto> content = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, Objects.requireNonNull(searchResponse.hits().total()).value());
    }

    public Page<CommentResponseDto> searchComments(Long boardId, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, size);

        Query query = TermQuery.of(t -> t.field("boardId").value(boardId))._toQuery();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("comments")
                .query(query)
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize())
                .sort(s -> s.field(f -> f.field("createdAt").order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)))
                .build();

        SearchResponse<CommentResponseDto> searchResponse = elasticsearchClient.search(searchRequest, CommentResponseDto.class);

        List<CommentResponseDto> content = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, Objects.requireNonNull(searchResponse.hits().total()).value());
    }

    public Page<?> search(String keyword, String category, Long boardId, int page, int size) throws Exception {
        if (boardId != null) {
            return searchComments(boardId, page, size);
        }
        return searchBoards(keyword, category, page, size);
    }
}
