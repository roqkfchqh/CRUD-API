package com.example.crud.interfaces.rest.board;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
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

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(
            HttpServletRequest req,
            @RequestBody BoardRequestDto dto){

        User sessionUser = (User) req.getSession().getAttribute("user");

        if (sessionUser != null) {
            //로그인 사용자
            boardService.createPost(sessionUser, dto);
        } else {
            //비로그인 사용자
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            if(nickname == null || password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            boardService.createPostForAnonymous(nickname, password, dto);
        }

        return ResponseEntity.ok(boardService.readPost(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> readPost(
            @PathVariable Long id){
        return ResponseEntity.ok(boardService.readPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateCount(
            @PathVariable Long id,
            @RequestBody BoardRequestDto dto){
        User sessionUser = (User) req.getSession().getAttribute("user");

        if (sessionUser != null) {
            //로그인 사용자
            boardService.updatePost(sessionUser, dto);
        } else {
            //비로그인 사용자
            String password = req.getParameter("password");

            if(password == null){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

            boardService.updatePostForAnonymous(password, dto);
        }

        return ResponseEntity.ok(boardService.updatePost(id, dto));
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<BoardResponseDto> likePost(@PathVariable Long id){
        return ResponseEntity.ok(boardService.likePost(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardResponseDto> deletePost(@PathVariable Long id, @RequestBody BoardPasswordRequestDto boardPasswordRequestDto){
        boardService.deletePost(id, boardPasswordRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pages")
    public ResponseEntity<Page<BoardResponseDto>> getPagedBoard(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingBoard(page, size));
    }

    @GetMapping("/category/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getCategoryBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingCategory(keyword, page, size));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Page<BoardResponseDto>> getSearchBoard(@PathVariable String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(boardService.pagingSearch(keyword, page, size));
    }
}
