package com.example.crud.controller.board.service.paging;

import com.example.crud.controller.board.entity.BoardDb;

import java.util.List;

public interface PagingService {
    List<BoardDb> getPagedBoards(int page, int size);
}
