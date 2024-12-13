package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.service.BoardPagingService;
import com.example.crud.domain.board_root.service.BoardService;

import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardPagingService boardPagingService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(
            HttpServletRequest req,
            @RequestBody BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if (sessionUser != null) {
            //로그인 사용자
            board = boardService.createPost(req, dto);
        } else {
            //비로그인 사용자
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            if(nickname == null || password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            board = boardService.createPostForAnonymous(nickname, password, dto);
        }

        return ResponseEntity.ok(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> readPost(
            @PathVariable Long id){
        return ResponseEntity.ok(boardService.readPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateCount(
            HttpServletRequest req,
            @PathVariable Long id,
            @RequestBody BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");
        BoardResponseDto board;

        if (sessionUser != null) {
            //로그인 사용자
            board = boardService.updatePost(req, id, dto);
        } else {
            //비로그인 사용자
            String password = req.getParameter("password");

            if(password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            board = boardService.updatePostForAnonymous(password, id, dto);
        }

        return ResponseEntity.ok(board);
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<BoardResponseDto> likePost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.likePost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardResponseDto> deletePost(
            HttpServletRequest req,
            @PathVariable Long id){
        boardService.deletePost(req, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<BoardResponseDto>> getPagedBoard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingBoard(page, size));
    }

    @GetMapping("/category/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getCategoryBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getSearchBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardPagingService.pagingSearch(keyword, page, size));
    }
}
