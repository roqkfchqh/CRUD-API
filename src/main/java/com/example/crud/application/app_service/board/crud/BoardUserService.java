package com.example.crud.application.app_service.board.crud;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardUserService implements BoardStrategy<Long> {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardDomainService boardDomainService;

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
        User user = getUser(userInfo);
        Comment comment = boardDomainService.createComment(user, user.getName(), dto.getContent(), board);
        commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId, Long userInfo) {
        Board board = getBoard(boardId);
        Comment comment = getComment(commentId);
        board.removeComment(comment);
        commentRepository.deleteById(commentId);
    }

    //helper
    private Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private User getUser(Long userInfo) {
        return userRepository.findById(userInfo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
