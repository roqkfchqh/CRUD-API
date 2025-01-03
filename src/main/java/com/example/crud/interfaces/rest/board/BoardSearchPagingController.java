package com.example.crud.interfaces.rest.board;

import com.example.crud.application.app_service.board.common.BoardSearchPagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardSearchPagingController {

    private static final String PAGE_COUNT = "1";
    private static final String PAGE_SIZE = "10";

    private final BoardSearchPagingService boardSearchPagingService;

    @GetMapping
    public ResponseEntity<Page<?>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long boardId,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) throws Exception {

        Page<?> results = boardSearchPagingService.search(keyword, category, boardId, page, size);
        return ResponseEntity.ok(results);
    }
}
