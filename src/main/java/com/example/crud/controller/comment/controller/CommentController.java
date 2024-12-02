package com.example.crud.controller.comment.controller;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.comment.dto.CommentRequestDto;
import com.example.crud.controller.comment.dto.CommentResponseDto;
import com.example.crud.controller.comment.service.CommentService;
import com.example.crud.controller.common.exception.CustomException;
import com.example.crud.controller.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardRepository boardRepository;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody Long boardId, @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.createComment(boardId, commentRequestDto));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsBoard(@PathVariable Long boardId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        BoardDb boardDb = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Page<CommentResponseDto> commentsPage = commentService.getCommentsBoard(boardDb, page, size);
        return ResponseEntity.ok(commentsPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody Long boardId, @PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.updateComment(boardId, id, commentRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(@RequestBody Long boardId, @PathVariable Long id, @RequestBody String password) {
        commentService.deleteComment(boardId, id, password);
        return ResponseEntity.noContent().build();
    }
}
