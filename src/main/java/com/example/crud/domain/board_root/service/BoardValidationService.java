package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
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

    @Transactional(readOnly = true)
    public Board validateBoard(Long id){
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    public Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        return PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
    }

    public void validatePassword(Long id, String password){
        String boardPassword = boardRepository.findPasswordById(id);
        if(!passwordEncoder.matches(password, boardPassword)){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void validateAnonymousUser(BoardRequestDto dto) {
        if (dto.getNickname() == null || dto.getNickname().isBlank()) {
            throw new CustomException(ErrorCode.NICKNAME_REQUIRED);
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }

        if (dto.getNickname().length() < 3 || dto.getNickname().length() > 10) {
            throw new CustomException(ErrorCode.NICKNAME_REQUIRED);
        }

        if (dto.getPassword().length() < 3 || dto.getPassword().length() > 10) {
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }
    }
}
