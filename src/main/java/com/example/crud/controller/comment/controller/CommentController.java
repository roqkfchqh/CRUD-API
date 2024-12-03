package com.example.crud.controller.comment.controller;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.comment.dto.CommentCombinedRequestDto;
import com.example.crud.controller.comment.dto.CommentPasswordRequestDto;
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
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentCombinedRequestDto commentCombinedRequestDto) {
        Long boardId = commentCombinedRequestDto.getCommentPasswordRequestDto().getBoardId();
        return ResponseEntity.ok(commentService.createComment(boardId, commentCombinedRequestDto));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsBoard(@PathVariable Long boardId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        BoardDb boardDb = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Page<CommentResponseDto> commentsPage = commentService.getCommentsBoard(boardDb, page, size);
        return ResponseEntity.ok(commentsPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentCombinedRequestDto commentCombinedRequestDto) {
        Long boardId = commentCombinedRequestDto.getCommentPasswordRequestDto().getBoardId();
        return ResponseEntity.ok(commentService.updateComment(boardId, id, commentCombinedRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long id, @RequestBody CommentPasswordRequestDto commentPasswordRequestDto) {
        Long boardId = commentPasswordRequestDto.getBoardId();
        commentService.deleteComment(boardId, id, commentPasswordRequestDto);
        return ResponseEntity.noContent().build();
    }
}
