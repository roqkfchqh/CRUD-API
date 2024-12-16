package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardPagingResponseDto;
import com.example.crud.domain.board_root.service.BoardPagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardPagingController {

    private static final String PAGE_COUNT = "0";
    private static final String PAGE_SIZE = "10";

    private final BoardPagingService boardPagingService;

    @GetMapping("/pages")
    public ResponseEntity<Page<BoardPagingResponseDto>> getPagedBoard(
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size){
        return ResponseEntity.ok(boardPagingService.pagingBoard(page, size));
    }

    @GetMapping("/category/{keyword}")
    public ResponseEntity<Page<BoardPagingResponseDto>> getCategoryBoard(
            @PathVariable String keyword,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size){
        return ResponseEntity.ok(boardPagingService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<BoardPagingResponseDto>> getSearchBoard(
            @PathVariable String keyword,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size){
        return ResponseEntity.ok(boardPagingService.pagingSearch(keyword, page, size));
    }
}
