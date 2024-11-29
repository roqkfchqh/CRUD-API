package com.example.crud.controller.board.service.paging;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;

import java.util.List;

public class JustPagingService implements PagingService {
    private final BoardRepository boardRepository;

    public JustPagingService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public List<BoardDb> getPagedBoards(int page, int size){
        int start = (page - 1) * size;
        int end = start + size;
        return boardRepository.findAll().subList(start, end);
    }
}
