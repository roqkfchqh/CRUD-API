package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.domain.board_root.aggregate.Board;
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
    private final BoardValidationService boardValidationService;

    public BoardResponseDto createPostForAnonymous(BoardRequestDto dto){
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Board board = BoardMapper.toEntityWithAnonymous(dto, encodedPassword);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    public BoardResponseDto updatePostForAnonymous(Long id, BoardRequestDto dto){
        boardValidationService.validatePassword(id, dto.getPassword());
        Board board = boardValidationService.validateBoard(id);

        board.updatePost(dto.getTitle(), dto.getContent(), dto.getCategory());
        boardRepository.save(board);

        return BoardMapper.toDto(board);
    }

    public void deletePostForAnonymous(BoardPasswordRequestDto dto, Long id){
        boardValidationService.validatePassword(id, dto.getPassword());
        boardValidationService.validateBoard(id);
        boardRepository.deleteById(id);
    }


}
