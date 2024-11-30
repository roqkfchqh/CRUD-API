package com.example.crud.controller.board.service;

import com.example.crud.controller.board.dto.BoardMapper;
import com.example.crud.controller.board.dto.BoardRequestDto;
import com.example.crud.controller.board.dto.BoardResponseDto;
import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.common.exception.BadInputException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //create
    public BoardResponseDto createPost(BoardRequestDto boardRequestDto) {
        BoardDb boardDb = BoardMapper.fromRequestDto(boardRequestDto);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //read
    public BoardResponseDto readPost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));
        boardDb.setCount(boardDb.getCount() + 1);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //update
    public BoardResponseDto updatePost(Long id, BoardRequestDto boardRequestDto) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));

        boardDb.setTitle(boardRequestDto.getTitle());
        boardDb.setContent(boardRequestDto.getContent());
        boardDb.setCategory(Category.valueOf(boardRequestDto.getCategory().toUpperCase()));

        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //like
    public BoardResponseDto likePost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new BadInputException("게시글 없음"));

        boardDb.setLiked(boardDb.getLiked() + 1);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //delete
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    //paging
    public List<BoardResponseDto> pagingBoard(int page, int size){
        extracted(page, size);
        List<BoardDb> boardDb = boardRepository.findAll();

        return getBoardResponseDto(page, size, boardDb);
    }

    //category paging
    public List<BoardResponseDto> pagingCategory(String category, int page, int size){
        extracted(page, size);
        List<BoardDb> boardDb = boardRepository.findByCategory(category.toUpperCase());

        return getBoardResponseDto(page, size, boardDb);
    }

    //search paging
    public List<BoardResponseDto> pagingSearch(String keyword, int page, int size){
        extracted(page, size);
        List<BoardDb> boardDb = boardRepository.findByTitleContaining(keyword);

        return getBoardResponseDto(page, size, boardDb);
    }

    private static void extracted(int page, int size) {
        if (page < 1 || size < 1) {
            throw new BadInputException("오류");
        }
    }

    private static List<BoardResponseDto> getBoardResponseDto(int page, int size, List<BoardDb> boardDb) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, boardDb.size());
        if (start >= boardDb.size()) {
            return List.of();
        }
        return boardDb.subList(start, end).stream()
                .map(BoardMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
