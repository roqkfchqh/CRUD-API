package com.example.crud.application.app_service.validation;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardValidationService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    public void validateBoard(Long id){
        boardRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public void validateComment(Long id) {
        commentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public void validateBoardPassword(Long id, String password){
        if(!passwordEncoder.matches(password, boardRepository.findPasswordById(id))){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void validateCommentPassword(Long id, String password){
        if(!passwordEncoder.matches(password, commentRepository.findPasswordById(id))){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void validateAnonymousUser(String nickname, String password){
        if (nickname == null || nickname.isBlank()){
            throw new CustomException(ErrorCode.NICKNAME_REQUIRED);
        }

        if (password == null || password.isBlank()){
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }

        if (nickname.length() < 3 || nickname.length() > 10){
            throw new CustomException(ErrorCode.NICKNAME_REQUIRED);
        }

        if (password.length() < 3 || password.length() > 10){
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }
    }
}
