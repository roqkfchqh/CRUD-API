package com.example.crud.application.app_service.board;

import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.domain_service.BoardDomainService;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import com.example.crud.domain.board_root.valueobjects.Category;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService implements BoardStrategy<Long>{

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardAsyncService boardAsyncService;
    private final BoardPagingService boardPagingService;
    private final BoardDomainService boardDomainService;
    private final UserRepository userRepository;

    @Override
    public BoardResponseDto createPost(BoardRequestDto dto, Long userInfo) {
        String userName = userRepository.findNameById(userInfo);
        Board board = Board.create(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()), userName);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    public BoardResponseDto updatePost(BoardRequestDto dto, Long userInfo, Long id) {
        Board board = getBoard(id);

        board.update(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()));

        return BoardMapper.toDto(board);
    }

    @Override
    public void deletePost(Long userInfo, Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto dto, Long userInfo) {
        Board board = getBoard(boardId);
        User user = userRepository.findById(userInfo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = boardDomainService.createComment(user, user.getName(), dto.getContent(), board);

        commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId, Long userInfo) {

        Board board = getBoard(boardId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
    }

    //readPost
    public BoardReadResponseDto readPost(Long id, int page, int size){
        Board board = getBoard(id);
        boardAsyncService.updateViewCountAsync(id);

        Page<CommentResponseDto> comments = boardPagingService.pagingComments(id, page, size);
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
