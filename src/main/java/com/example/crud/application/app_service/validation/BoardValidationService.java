package com.example.crud.application.app_service.validation;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardValidationService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Board validateBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public Comment validateComment(Long id, Board board) {
        return board.getComments().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        return PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
    }

    public Board validateBoardPassword(Long id, String password){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        String boardPassword = board.getPassword();
        if(!passwordEncoder.matches(password, boardPassword)){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        return board;
    }

    public void validateCommentPassword(Long id, String password){
        String commentPassword = String.valueOf(commentRepository.findById(id));
        if(!passwordEncoder.matches(password, commentPassword)){
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
