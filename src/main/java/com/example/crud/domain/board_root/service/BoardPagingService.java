package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardPagingService {

    private final BoardRepository boardRepository;
    private final BoardValidationService boardValidationService;
    private final CacheService cacheService;

    //paging
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> pagingBoard(int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        cacheService.pagePut(page, size);

        return boardRepository.findAll(pageable)
                .map(BoardMapper::toDto);
    }

    //category paging
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> pagingCategory(String category, int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        cacheService.categoryPut(category, page, size);

        return boardRepository.findByCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toDto);
    }

    //search paging
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> pagingSearch(String keyword, int page, int size){
        Pageable pageable = boardValidationService.validatePageSize(page, size);

        cacheService.searchPut(keyword, page, size);

        return boardRepository.findByTitleContaining(keyword, pageable)
                .map(BoardMapper::toDto);
    }
}
