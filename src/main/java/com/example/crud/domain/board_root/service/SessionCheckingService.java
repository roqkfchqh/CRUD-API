package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SessionCheckingService {

    private final BoardService boardService;
    private final BoardAnonymousService boardAnonymousService;

    public BoardResponseDto getCreatePost(
            HttpServletRequest req,
            BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser != null){
            //로그인 사용자
            board = boardService.createPost(req, dto);
        }else{
            //비로그인 사용자
            if(dto.getNickname() == null || dto.getPassword() == null){
                throw new CustomException(ErrorCode.REQUEST_REQUIRED);
            }
            board = boardAnonymousService.createPostForAnonymous(dto);
        }
        return board;
    }

    public BoardResponseDto getUpdatePost(
            HttpServletRequest req,
            Long id,
            BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser != null){
            //로그인 사용자
            board = boardService.updatePost(req, id, dto);
        }else{
            //비로그인 사용자
            if(dto.getPassword() == null){
                throw new CustomException(ErrorCode.REQUEST_REQUIRED);
            }
            board = boardAnonymousService.updatePostForAnonymous(id, dto);
        }
        return board;
    }

    public void getDeletePost(
            HttpServletRequest req,
            Long id,
            BoardPasswordRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");

        if(sessionUser != null){
            //로그인 사용자
            boardService.deletePost(req, id);
        }else{
            //비로그인 사용자
            if(dto.getPassword() == null){
                throw new CustomException(ErrorCode.REQUEST_REQUIRED);
            }
            boardAnonymousService.deletePostForAnonymous(dto, id);
        }
    }
}
