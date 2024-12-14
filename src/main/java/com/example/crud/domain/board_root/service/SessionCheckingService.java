package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SessionCheckingService {

    private final BoardService boardService;
    private final BoardAnonymousService boardAnonymousService;

    public BoardResponseDto CreatePost(
            HttpServletRequest req,
            BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser == null){
            board = boardAnonymousService.createPostForAnonymous(dto);
        }else{
            board = boardService.createPost(req, dto);
        }
        return board;
    }

    public BoardResponseDto UpdatePost(
            HttpServletRequest req,
            Long id,
            BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser == null){
            board = boardAnonymousService.updatePostForAnonymous(id, dto);
        }else{
            board = boardService.updatePost(req, id, dto);
        }
        return board;
    }

    public void DeletePost(
            HttpServletRequest req,
            Long id,
            BoardPasswordRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");

        if(sessionUser == null){
            boardAnonymousService.deletePostForAnonymous(dto, id);
        }else{
            boardService.deletePost(req, id);
        }
    }

    public CommentResponseDto CreateComment(
            HttpServletRequest req,
            CommentRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        CommentResponseDto comment;

        if(sessionUser == null){
            comment = boardAnonymousService.createCommentForAnonymous(dto);
        }else{
            comment = boardService.createComment(req, dto);
        }
        return comment;
    }

    public void DeleteComment(
            HttpServletRequest req,
            Long id,
            CommentPasswordRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");

        if(sessionUser == null){
            boardAnonymousService.deleteCommentForAnonymous(id, dto);
        }else{
            boardService.deleteComment(req, dto, id);
        }
    }
}
