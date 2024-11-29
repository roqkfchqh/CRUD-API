package com.example.crud.controller.board.service.paging;

import com.example.crud.controller.board.entity.BoardDb;

import java.util.stream.Collectors;
import java.util.List;

public class SearchPagingService implements PagingService {
    private final PagingService pagingService;
    private final String keyword;

    public SearchPagingService(PagingService pagingService, String keyword) {
        this.pagingService = pagingService;
        this.keyword = keyword;
    }

    @Override
    public List<BoardDb> getPagedBoards(int page, int size){
        List<BoardDb> board = pagingService.getPagedBoards(page, size);
        return board.stream()
                .filter(boardDb -> boardDb.getTitle().contains(keyword))
                .collect(Collectors.toList());
    }
}
