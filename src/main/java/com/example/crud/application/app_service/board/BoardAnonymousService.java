package com.example.crud.application.app_service.board;

import com.example.crud.application.dto.board.AnonymousRequestDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardAnonymousService implements BoardStrategy<AnonymousRequestDto> {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;
    private final BoardDomainService boardDomainService;

    @Override
    @Transactional
    public BoardResponseDto createPost(BoardRequestDto dto, AnonymousRequestDto userInfo) {
        String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
        Board board = Board.create(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()), userInfo.getNickname(), encodedPassword);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    @Transactional
    public BoardResponseDto updatePost(BoardRequestDto dto, AnonymousRequestDto userInfo, Long id) {
        Board board = getBoard(id);

        board.update(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()));

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    public void deletePost(AnonymousRequestDto userInfo, Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(Long boardId, CommentRequestDto dto, AnonymousRequestDto userInfo) {
        String encodedPassword = passwordEncoder.encode(userInfo.getPassword());

        Board board = getBoard(boardId);

        Comment comment = boardDomainService.createAnonymousComment(userInfo.getNickname(), dto.getContent(), board, encodedPassword);

        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId, AnonymousRequestDto userInfo) {

        Board board = getBoard(boardId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }

    //helper
    private Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}
