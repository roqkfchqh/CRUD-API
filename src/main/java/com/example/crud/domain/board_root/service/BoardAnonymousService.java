package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import com.example.crud.infrastructure.cache.CustomCacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardAnonymousService extends AbstractBoardService{

    private static final int POST_TTL = 600;

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardValidationService boardValidationService;
    private final CommentRepository commentRepository;

    @Override
    protected void validateUser(Object userInfo){
        BoardRequestDto dto = (BoardRequestDto) userInfo;
        boardValidationService.validateAnonymousUser(dto.getNickname(), dto.getPassword());
    }

    @Override
    protected void validateUserForDelete(Object userInfo, Long id){
        BoardPasswordRequestDto dto = (BoardPasswordRequestDto) userInfo;
        boardValidationService.validateBoardPassword(id, dto.getPassword());
    }

    //createPost
    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Board board = BoardMapper.toEntityWithAnonymous(dto, encodedPassword);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //updatePost
    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Object userInfo, Long id){
        Board board = boardValidationService.validateBoardPassword(id, dto.getPassword());

        board.updatePost(dto.getTitle(), dto.getContent(), dto.getCategory());
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Object userInfo, Long id){
        boardRepository.deleteById(id);
    }

    //createComment
    @CustomCacheable(key = "'post::' + #id", ttl = POST_TTL)
    public CommentResponseDto createCommentForAnonymous(CommentRequestDto dto){
        boardValidationService.validateAnonymousUser(dto.getNickname(), dto.getPassword());
        Board board = boardValidationService.validateBoard(dto.getBoardId());

        Comment comment = CommentMapper.toEntityWithAnonymous(dto, board);

        board.addComment(comment);
        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    @CustomCacheable(key = "'post::' + #id", ttl = POST_TTL)
    public void deleteCommentForAnonymous(Long commentId, CommentPasswordRequestDto dto){
        boardValidationService.validateCommentPassword(commentId, dto.getPassword());

        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }
}
