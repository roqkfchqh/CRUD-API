package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

        HttpSession session = req.getSession(false);
        BoardResponseDto board;

        if(session == null){
            board = boardAnonymousService.createPost(dto, dto);
        }else{
            board = boardService.createPost(dto, req);
        }
        return board;
    }

    public BoardResponseDto UpdatePost(
            HttpServletRequest req,
            Long id,
            BoardRequestDto dto){

        HttpSession session = req.getSession(false);
        BoardResponseDto board;

        if(session == null){
            board = boardAnonymousService.updatePost(dto, dto, id);
        }else{
            board = boardService.updatePost(dto, req, id);
        }
        return board;
    }

    public void DeletePost(
            HttpServletRequest req,
            Long id,
            BoardPasswordRequestDto dto){

        HttpSession session = req.getSession(false);

        if(session == null){
            boardAnonymousService.deletePost(dto, id);
        }else{
            boardService.deletePost(req, id);
        }
    }

    public CommentResponseDto CreateComment(
            HttpServletRequest req,
            CommentRequestDto dto){

        HttpSession session = req.getSession(false);
        CommentResponseDto comment;

        if(session == null){
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

        HttpSession session = req.getSession(false);

        if(session == null){
            boardAnonymousService.deleteCommentForAnonymous(id, dto);
        }else{
            boardService.deleteComment(req, dto, id);
        }
    }
}
