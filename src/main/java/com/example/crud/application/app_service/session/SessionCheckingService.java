package com.example.crud.application.app_service.session;

import com.example.crud.application.app_service.board.BoardAnonymousService;
import com.example.crud.application.app_service.board.BoardService;
import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.user_root.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SessionCheckingService {

    private final BoardService boardService;
    private final BoardAnonymousService boardAnonymousService;

    public BoardResponseDto CreatePost(
            User user,
            BoardRequestDto dto){

        BoardResponseDto board;

        if(user == null){
            board = boardAnonymousService.createPost(dto, dto);
        }else{
            board = boardService.createPost(dto, user);
        }
        return board;
    }

    public BoardResponseDto UpdatePost(
            User user,
            Long id,
            BoardRequestDto dto){

        BoardResponseDto board;

        if(user == null){
            board = boardAnonymousService.updatePost(dto, dto, id);
        }else{
            board = boardService.updatePost(dto, user, id);
        }
        return board;
    }

    public void DeletePost(
            User user,
            Long id,
            BoardPasswordRequestDto dto){

        if(user == null){
            boardAnonymousService.deletePost(dto, id);
        }else{
            boardService.deletePost(user, id);
        }
    }

    public CommentResponseDto CreateComment(
            User user,
            CommentRequestDto dto){

        CommentResponseDto comment;

        if(user == null){
            comment = boardAnonymousService.createCommentForAnonymous(dto);
        }else{
            comment = boardService.createComment(user, dto);
        }
        return comment;
    }

    public void DeleteComment(
            User user,
            Long id,
            CommentPasswordRequestDto dto){

        if(user == null){
            boardAnonymousService.deleteCommentForAnonymous(id, dto);
        }else{
            boardService.deleteComment(dto, id);
        }
    }
}
