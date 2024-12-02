package com.example.crud.controller.board.controller;

import com.example.crud.controller.board.dto.BoardCombinedRequestDto;
import com.example.crud.controller.board.dto.BoardResponseDto;
import com.example.crud.controller.board.dto.BoardPasswordRequestDto;
import com.example.crud.controller.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(@RequestBody BoardCombinedRequestDto boardCombinedRequestDto){
        return ResponseEntity.ok(boardService.createPost(boardCombinedRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> readPost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.readPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateCount(@PathVariable Long id, @RequestBody BoardCombinedRequestDto boardCombinedRequestDto){
        return ResponseEntity.ok(boardService.updatePost(id, boardCombinedRequestDto));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<BoardResponseDto> likePost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.likePost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardResponseDto> deletePost(@PathVariable Long id, @RequestBody BoardPasswordRequestDto boardPasswordRequestDto){
        boardService.deletePost(id, boardPasswordRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<BoardResponseDto>> getPagedBoard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingBoard(page, size));
    }

    @GetMapping("/page/category/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getCategoryBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/page/search/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getSearchBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingSearch(keyword, page, size));
    }
}
