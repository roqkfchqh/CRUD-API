package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardAnonymousService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    public BoardResponseDto createPostForAnonymous(String nickname, String password, BoardRequestDto dto){



        return null;
    }

    public BoardResponseDto updatePostForAnonymous(String password, Long id, BoardRequestDto dto){
        return null;
    }

    public void deletePostForAnonymous(String password, Long id){

    }


}
