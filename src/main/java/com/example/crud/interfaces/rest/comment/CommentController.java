package com.example.crud.interfaces.rest.comment;

import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.app_service.session.SessionCheckingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final SessionCheckingService sessionCheckingService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            HttpServletRequest req,
            @RequestBody CommentRequestDto dto){

        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        CommentResponseDto comment = sessionCheckingService.CreateComment(sessionUserId, dto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            HttpServletRequest req,
            @PathVariable Long id,
            @RequestBody CommentPasswordRequestDto dto){

        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        sessionCheckingService.DeleteComment(sessionUserId, id, dto);
        return ResponseEntity.noContent().build();
    }
}
