package com.example.crud.application.app_service.board.common;

import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardAsyncService boardAsyncService;
    private final BoardSearchPagingService boardSearchPagingService;
    private final BoardRepository boardRepository;

    //readPost
    public BoardReadResponseDto readPost(Long id, int page, int size){
        Board board = getBoard(id);
        boardAsyncService.updateViewCountAsync(id);

        Page<CommentResponseDto> comments = boardSearchPagingService.pagingComments(id, page, size);
        return BoardMapper.toReadDto(board, comments);
    }

    //likePost
    @Transactional
    public BoardResponseDto likePost(Long id) {
        Board board = getBoard(id);

        board.updateLiked();
        return BoardMapper.toDto(board);
    }

    //helper
    private Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}
