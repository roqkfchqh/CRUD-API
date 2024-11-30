package com.example.crud.controller.comment.controller;

import com.example.crud.controller.board.service.BoardService;
import com.example.crud.controller.comment.dto.CommentRequestDto;
import com.example.crud.controller.comment.dto.CommentResponseDto;
import com.example.crud.controller.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService, BoardService boardService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.create(commentRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @PutMapping
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(id, commentRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<CommentResponseDto> deleteComment(@RequestBody Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
