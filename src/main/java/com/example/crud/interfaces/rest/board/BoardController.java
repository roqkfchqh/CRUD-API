package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.AnonymousRequestDto;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.app_service.board.BoardService;
import com.example.crud.application.app_service.session.SessionCheckingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private static final String PAGE_COUNT = "0";
    private static final String PAGE_SIZE = "10";

    private final BoardService boardService;
    private final SessionCheckingService sessionCheckingService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(
            HttpServletRequest req,
            @Valid @RequestBody BoardRequestDto dto,
            @RequestHeader(required = false) AnonymousRequestDto anonymous
    ){
        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        BoardResponseDto board = sessionCheckingService.CreatePost(sessionUserId, dto, anonymous);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardReadResponseDto> readPost(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ){
        return ResponseEntity.ok(boardService.readPost(boardId, page, size));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updatePost(
            HttpServletRequest req,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto dto,
            @RequestHeader(required = false) AnonymousRequestDto anonymous
    ){
        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        BoardResponseDto board = sessionCheckingService.UpdatePost(sessionUserId, boardId, dto, anonymous);

        return ResponseEntity.ok(board);
    }

    @PutMapping("/{boardId}/likes")
    public ResponseEntity<BoardResponseDto> likePost(
            @PathVariable Long boardId
    ){
        return ResponseEntity.ok(boardService.likePost(boardId));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deletePost(
            HttpServletRequest req,
            @PathVariable Long boardId,
            @RequestHeader(required = false) AnonymousRequestDto anonymous
    ){
        Long sessionUserId = (Long) req.getSession().getAttribute("userId");

        sessionCheckingService.DeletePost(sessionUserId, boardId, anonymous);

        return ResponseEntity.ok("해당 게시물이 정상적으로 삭제되었습니다.");
    }
}
