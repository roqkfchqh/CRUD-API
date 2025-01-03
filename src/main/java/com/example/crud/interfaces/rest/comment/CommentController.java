package com.example.crud.interfaces.rest.comment;

import com.example.crud.application.dto.board.AnonymousRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.app_service.sessioncheck.SessionCheckingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class CommentController {

    private final SessionCheckingService sessionCheckingService;

    @PostMapping("/{boardId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long boardId,
            HttpServletRequest req,
            @RequestBody CommentRequestDto dto,
            @RequestHeader(required = false) AnonymousRequestDto anonymous) {

        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        CommentResponseDto comment = sessionCheckingService.CreateComment(boardId, sessionUserId, dto, anonymous);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @PathVariable Long boardId,
            HttpServletRequest req,
            @PathVariable Long commentId,
            @RequestHeader(required = false) AnonymousRequestDto anonymous){

        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        sessionCheckingService.DeleteComment(boardId, sessionUserId, commentId, anonymous);
        return ResponseEntity.noContent().build();
    }
}
