package com.example.crud.application.app_service.session;

import com.example.crud.application.app_service.board.BoardAnonymousService;
import com.example.crud.application.app_service.board.BoardService;
import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
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
            String userId,
            BoardRequestDto dto){

        BoardResponseDto board;

        if(userId == null){
            board = boardAnonymousService.createPost(dto, dto);
        }else{
            board = boardService.createPost(dto, userId);
        }
        return board;
    }

    public BoardResponseDto UpdatePost(
            String userId,
            Long id,
            BoardRequestDto dto){

        BoardResponseDto board;

        if(userId == null){
            board = boardAnonymousService.updatePost(dto, dto, id);
        }else{
            board = boardService.updatePost(dto, userId, id);
        }
        return board;
    }

    public void DeletePost(
            String userId,
            Long id,
            BoardPasswordRequestDto dto){

        if(userId == null){
            boardAnonymousService.deletePost(dto, id);
        }else{
            boardService.deletePost(userId, id);
        }
    }

    public CommentResponseDto CreateComment(
            String userId,
            CommentRequestDto dto){

        CommentResponseDto comment;

        if(userId == null){
            comment = boardAnonymousService.createCommentForAnonymous(dto);
        }else{
            comment = boardService.createComment(userId, dto);
        }
        return comment;
    }

    public void DeleteComment(
            String userId,
            Long id,
            CommentPasswordRequestDto dto){

        if(userId == null){
            boardAnonymousService.deleteCommentForAnonymous(id, dto);
        }else{
            boardService.deleteComment(dto, id);
        }
    }
}
