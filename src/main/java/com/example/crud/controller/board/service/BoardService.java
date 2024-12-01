package com.example.crud.controller.board.service;

import com.example.crud.controller.board.dto.BoardMapper;
import com.example.crud.controller.board.dto.BoardRequestDto;
import com.example.crud.controller.board.dto.BoardResponseDto;
import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.common.exception.BadInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    //createComment
    public BoardResponseDto createPost(BoardRequestDto boardRequestDto) {
        String encodedPassword = passwordEncoder.encode(boardRequestDto.getPassword());
        BoardDb boardDb = BoardMapper.fromRequestDto(boardRequestDto);
        boardDb.setPassword(encodedPassword);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //read
    public BoardResponseDto readPost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));
        boardDb.updateCount(boardDb.getCount() + 1);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //update
    public BoardResponseDto updatePost(Long id, BoardRequestDto boardRequestDto) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));
        if (!passwordEncoder.matches(boardRequestDto.getPassword(), boardDb.getPassword())) {
            throw new BadInputException("비밀번호가 일치하지 않습니다.");
        }

        boardDb.updatePost(boardRequestDto.getContent(), boardRequestDto.getTitle(), boardRequestDto.getCategory());

        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //like
    public BoardResponseDto likePost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));

        boardDb.updateLiked(boardDb.getLiked() + 1);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //delete
    public void deletePost(Long id, String password) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));
        if (!passwordEncoder.matches(password, boardDb.getPassword())) {
            throw new BadInputException("비밀번호가 일치하지 않습니다.");
        }
        boardRepository.deleteById(id);
    }

    //paging
    public Page<BoardResponseDto> pagingBoard(int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page -1, size, Sort.by("createDate").descending());
        return boardRepository.findAllBoard(pageable)
                .map(BoardMapper::toResponseDto);
    }

    //category paging
    public Page<BoardResponseDto> pagingCategory(String category, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending());
        return boardRepository.findAllCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toResponseDto);
    }

    //search paging
    public Page<BoardResponseDto> pagingSearch(String keyword, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending());
        return boardRepository.findAllSearch(keyword, pageable)
                .map(BoardMapper::toResponseDto);
    }

    private static void extracted(int page, int size) {
        if (page < 1 || size < 1) {
            throw new BadInputException("오류");
        }
    }

}
