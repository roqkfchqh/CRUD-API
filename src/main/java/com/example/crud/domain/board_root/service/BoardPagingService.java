package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPagingResponseDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import com.example.crud.domain.board_root.valueobjects.CommentSort;
import com.example.crud.infrastructure.cache.CustomCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.crud.domain.board_root.valueobjects.CommentSort.DESC;

@Service
@RequiredArgsConstructor
public class BoardPagingService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardValidationService boardValidationService;

    //paging
    @Transactional(readOnly = true)
    @CustomCacheable(key = "'boardP::' + #page + #size")
    public Page<BoardPagingResponseDto> pagingBoard(int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        return boardRepository.findAll(pageable)
                .map(BoardMapper::toPagingDto);
    }

    //category paging
    @Transactional(readOnly = true)
    @CustomCacheable(key = "'boardC::' + #category + #page + #size", ttl = 240)
    public Page<BoardPagingResponseDto> pagingCategory(String category, int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        return boardRepository.findByCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toPagingDto);
    }

    //search paging
    @Transactional(readOnly = true)
    @CustomCacheable(key = "'boardS::' + #keyword + #page + #size", ttl = 240)
    public Page<BoardPagingResponseDto> pagingSearch(String keyword, int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        return boardRepository.findByTitleContaining(keyword, pageable)
                .map(BoardMapper::toPagingDto);
    }

    //comment paging
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> pagingComments(CommentSort sort, Long boardId, Integer page, Integer size){
        if(page < 1 || size < 1){
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }

        Pageable pageable;
        if(sort == DESC){
            pageable = PageRequest.of(page, size, Sort.by("created").descending());
        }else{
            pageable = PageRequest.of(page, size, Sort.by("created").ascending());
        }

        Board board = boardValidationService.validateBoard(boardId);

        Page<CommentResponseDto> comments = commentRepository.findCommentsByBoard(board, pageable);
        if(comments.isEmpty()){
            return null;
        }
        return comments;
    }
}
