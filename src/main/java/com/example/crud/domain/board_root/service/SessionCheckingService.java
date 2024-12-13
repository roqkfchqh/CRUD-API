package com.example.crud.domain.board_root.service;

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

    public BoardResponseDto getCreatePost(HttpServletRequest req, BoardRequestDto dto) {
        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser != null){
            //로그인 사용자
            board = boardService.createPost(req, dto);
        }else{
            //비로그인 사용자
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            if(nickname == null || password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            board = boardAnonymousService.createPostForAnonymous(nickname, password, dto);
        }
        return board;
    }

    public BoardResponseDto getUpdatePost(HttpServletRequest req, Long id, BoardRequestDto dto) {
        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if(sessionUser != null){
            //로그인 사용자
            board = boardService.updatePost(req, id, dto);
        }else{
            //비로그인 사용자
            String password = req.getParameter("password");

            if(password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            board = boardAnonymousService.updatePostForAnonymous(password, id, dto);
        }
        return board;
    }

    public void getDeletePost(HttpServletRequest req, Long id) {
        User sessionUser = (User) req.getSession().getAttribute("user");

        if(sessionUser != null){
            //로그인 사용자
            boardService.deletePost(req, id);
        }else{
            //비로그인 사용자
            String password = req.getParameter("password");

            if(password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            boardAnonymousService.deletePostForAnonymous(password, id);
        }
    }
}
