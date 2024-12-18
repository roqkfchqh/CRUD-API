package com.example.crud.application.app_service.board;

import com.example.crud.application.dto.board.BoardPagingResponseDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardPagingService {

    //TODO: search 기능 합쳐야됨

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //paging
    @Transactional(readOnly = true)
    public Page<BoardPagingResponseDto> pagingBoard(int page, int size){
        Pageable pageable = validatePageSize(page, size);

        return boardRepository.findAll(pageable)
                .map(BoardMapper::toPagingDto);
    }

    //category paging
    @Transactional(readOnly = true)
    public Page<BoardPagingResponseDto> pagingCategory(String category, int page, int size){
        Pageable pageable = validatePageSize(page, size);

        return boardRepository.findByCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toPagingDto);
    }

    //search paging
    public Page<BoardPagingResponseDto> pagingSearch(String keyword, int page, int size){
        Pageable pageable = validatePageSize(page, size);

        return boardRepository.findByTitleContaining(keyword, pageable)
                .map(BoardMapper::toPagingDto);
    }

    //comment paging
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> pagingComments(Long boardId, int page, int size){
        Pageable pageable = validatePageSize(page, size);

        Page<Comment> comments = commentRepository.findCommentsByBoardId(boardId, pageable);
        if(comments.isEmpty()){
            return null;
        }
        return comments.map(CommentMapper::toDto);
    }

    public Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }
}
