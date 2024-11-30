package com.example.crud.controller.board.controller;

import com.example.crud.controller.board.dto.BoardRequestDto;
import com.example.crud.controller.board.dto.BoardResponseDto;
import com.example.crud.controller.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(@RequestBody BoardRequestDto boardRequestDto){
        return ResponseEntity.ok(boardService.createPost(boardRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> readPost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.readPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateCount(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto){
        return ResponseEntity.ok(boardService.updatePost(id, boardRequestDto));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<BoardResponseDto> likePost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.likePost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardResponseDto> deletePost(@PathVariable Long id){
        boardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<List<BoardResponseDto>> getPagedBoard(@RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(boardService.pagingBoard(page, size));
    }

    @GetMapping("/page/category/{keyword}")
    public ResponseEntity<List<BoardResponseDto>> getCategoryBoard(@PathVariable String keyword, @RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(boardService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/page/search/{keyword}")
    public ResponseEntity<List<BoardResponseDto>> getSearchBoard(@PathVariable String keyword, @RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(boardService.pagingSearch(keyword, page, size));
    }
}
