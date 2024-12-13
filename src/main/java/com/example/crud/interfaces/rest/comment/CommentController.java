package com.example.crud.interfaces.rest.comment;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.application.dto.comment.CommentCombinedRequestDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.service.CommentService;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
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
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Page<CommentResponseDto> commentsPage = commentService.getCommentsBoard(board, page, size);
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
