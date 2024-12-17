package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.app_service.board.BoardService;
import com.example.crud.application.app_service.session.SessionCheckingService;
import com.example.crud.domain.user_root.aggregate.User;
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
            @Valid @RequestBody BoardRequestDto dto){
        User sessionUser = (User) req.getSession().getAttribute("user");

        BoardResponseDto board = sessionCheckingService.CreatePost(sessionUser, dto);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReadResponseDto> readPost(
            @PathVariable Long id,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size){
        return ResponseEntity.ok(boardService.readPost(id, page, size));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updatePost(
            HttpServletRequest req,
            @PathVariable Long id,
            @Valid @RequestBody BoardRequestDto dto){
        User sessionUser = (User) req.getSession().getAttribute("user");

        BoardResponseDto board = sessionCheckingService.UpdatePost(sessionUser, id, dto);

        return ResponseEntity.ok(board);
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<BoardResponseDto> likePost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.likePost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            HttpServletRequest req,
            @PathVariable Long id,
            @Valid @RequestBody(required = false) BoardPasswordRequestDto dto){
        User sessionUser = (User) req.getSession().getAttribute("user");

        sessionCheckingService.DeletePost(sessionUser, id, dto);

        return ResponseEntity.ok("해당 게시물이 정상적으로 삭제되었습니다.");
    }
}
