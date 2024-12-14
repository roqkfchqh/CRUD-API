package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.domain.board_root.service.BoardService;
import com.example.crud.domain.board_root.service.SessionCheckingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final SessionCheckingService sessionCheckingService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(
            HttpServletRequest req,
            @Valid @RequestBody BoardRequestDto dto){

        BoardResponseDto board = sessionCheckingService.getCreatePost(req, dto);

        return ResponseEntity.ok(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> readPost(
            @PathVariable Long id){
        return ResponseEntity.ok(boardService.readPost(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updatePost(
            HttpServletRequest req,
            @PathVariable Long id,
            @Valid @RequestBody BoardRequestDto dto){

        BoardResponseDto board = sessionCheckingService.getUpdatePost(req, id, dto);

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

        sessionCheckingService.getDeletePost(req, id, dto);

        return ResponseEntity.ok("해당 게시물이 정상적으로 삭제되었습니다.");
    }
}
