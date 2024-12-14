package com.example.crud.interfaces.rest.comment;

import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.service.SessionCheckingService;
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

        CommentResponseDto comment = sessionCheckingService.CreateComment(req, dto);
        return ResponseEntity.ok(comment);
    }

//    @GetMapping("/{boardId}")
//    public ResponseEntity<Page<CommentResponseDto>> getCommentsBoard(
//            @PathVariable Long boardId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
//
//        Page<CommentResponseDto> commentsPage = commentService.getCommentsBoard(board, page, size);
//        return ResponseEntity.ok(commentsPage);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CommentResponseDto> updateComment(
//            @PathVariable Long id,
//            @RequestBody CommentCombinedRequestDto commentCombinedRequestDto) {
//        Long boardId = commentCombinedRequestDto.getCommentPasswordRequestDto().getBoardId();
//        return ResponseEntity.ok(commentService.updateComment(boardId, id, commentCombinedRequestDto));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            HttpServletRequest req,
            @PathVariable Long id,
            @RequestBody CommentPasswordRequestDto dto){

        sessionCheckingService.DeleteComment(req, id, dto);
        return ResponseEntity.noContent().build();
    }
}
