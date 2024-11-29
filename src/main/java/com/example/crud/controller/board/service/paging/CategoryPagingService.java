package com.example.crud.controller.board.service.paging;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;

import java.util.stream.Collectors;
import java.util.List;

public class CategoryPagingService implements PagingService {
    private final PagingService pagingService;
    private final Category category;

    public CategoryPagingService(PagingService pagingService, Category category) {
        this.pagingService = pagingService;
        this.category = category;
    }

    @Override
    public List<BoardDb> getPagedBoards(int page, int size){
        List<BoardDb> board = pagingService.getPagedBoards(page, size);
        return board.stream()
                .filter(boardDb -> boardDb.getCategory() == category)
                .collect(Collectors.toList());
    }
}