package com.example.crud.interfaces.rest.comment;

import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.app_service.session.SessionCheckingService;
import com.example.crud.domain.user_root.aggregate.User;
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

        User sessionUser = (User) req.getSession().getAttribute("user");

        CommentResponseDto comment = sessionCheckingService.CreateComment(sessionUser, dto);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            HttpServletRequest req,
            @PathVariable Long id,
            @RequestBody CommentPasswordRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");

        sessionCheckingService.DeleteComment(sessionUser, id, dto);
        return ResponseEntity.noContent().build();
    }
}
