package com.example.crud.application.app_service.board.crud;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;

public interface BoardStrategy<U> {

    BoardResponseDto createPost(BoardRequestDto dto, U userInfo);

    BoardResponseDto updatePost(BoardRequestDto dto, U userInfo, Long id);

    void deletePost(U userInfo, Long id);

    CommentResponseDto createComment(Long boardId, CommentRequestDto dto, U userInfo);

    void deleteComment(Long boardId, Long commentId, U userInfo);
}
