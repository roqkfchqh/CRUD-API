package com.example.crud.application.app_service.session;

import com.example.crud.application.app_service.board.BoardAnonymousService;
import com.example.crud.application.app_service.board.BoardService;
import com.example.crud.application.dto.board.AnonymousRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SessionCheckingService {

    private final BoardService boardService;
    private final BoardAnonymousService boardAnonymousService;

    public BoardResponseDto CreatePost(
            Long userId,
            BoardRequestDto dto,
            AnonymousRequestDto anonymous){

        BoardResponseDto board;

        if(userId == null){
            board = boardAnonymousService.createPost(dto, anonymous);
        }else{
            board = boardService.createPost(dto, userId);
        }
        return board;
    }

    public BoardResponseDto UpdatePost(
            Long userId,
            Long id,
            BoardRequestDto dto,
            AnonymousRequestDto anonymous){

        BoardResponseDto board;

        if(userId == null){
            board = boardAnonymousService.updatePost(dto, anonymous, id);
        }else{
            board = boardService.updatePost(dto, userId, id);
        }
        return board;
    }

    public void DeletePost(
            Long userId,
            Long id,
            AnonymousRequestDto dto){

        if(userId == null){
            boardAnonymousService.deletePost(dto, id);
        }else{
            boardService.deletePost(userId, id);
        }
    }

    public CommentResponseDto CreateComment(
            Long boardId,
            Long userId,
            CommentRequestDto dto,
            AnonymousRequestDto anonymous){

        CommentResponseDto comment;

        if(userId == null){
            comment = boardAnonymousService.createComment(boardId, dto, anonymous);
        }else{
            comment = boardService.createComment(boardId, dto, userId);
        }
        return comment;
    }

    public void DeleteComment(
            Long boardId,
            Long userId,
            Long commentId,
            AnonymousRequestDto anonymous){

        if(userId == null){
            boardAnonymousService.deleteComment(boardId, commentId, anonymous);
        }else{
            boardService.deleteComment(boardId, commentId, userId);
        }
    }
}
