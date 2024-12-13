package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardResponseDto;
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

    private final BoardPagingService boardPagingService;

    @GetMapping("/pages")
    public ResponseEntity<Page<BoardResponseDto>> getPagedBoard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingBoard(page, size));
    }

    @GetMapping("/category/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getCategoryBoard(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getSearchBoard(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingSearch(keyword, page, size));
    }
}
