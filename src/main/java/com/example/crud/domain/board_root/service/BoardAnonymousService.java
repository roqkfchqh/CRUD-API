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
import com.example.crud.infrastructure.cache.CacheEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardAnonymousService extends AbstractBoardService{

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardValidationService boardValidationService;
    private final CacheService cacheService;

    @Override
    protected void validateUser(Object userInfo){
        BoardRequestDto dto = (BoardRequestDto) userInfo;
        boardValidationService.validateAnonymousUser(dto.getNickname(), dto.getPassword());
    }

    @Override
    protected void validateUserForDelete(Object userInfo, Long id){
        boardValidationService.validateBoard(id);

        BoardPasswordRequestDto dto = (BoardPasswordRequestDto) userInfo;
        boardValidationService.validateBoardPassword(id, dto.getPassword());
    }

    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Board board = BoardMapper.toEntityWithAnonymous(dto, encodedPassword);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Object userInfo, Long id){
        boardValidationService.validateBoardPassword(id, dto.getPassword());
        Board board = boardValidationService.validateBoard(id);

        board.updatePost(dto.getTitle(), dto.getContent(), dto.getCategory());
        boardRepository.save(board);
        cacheService.put(board.getId(), CacheEventType.POST_PUT);
        return BoardMapper.toDto(board);
    }

    @Override
    protected void executeDeletePost(Object userInfo, Long id){
        boardRepository.deleteById(id);
        cacheService.evict(id, CacheEventType.POST_EVICT);
    }

    public CommentResponseDto createCommentForAnonymous(CommentRequestDto dto){
        boardValidationService.validateAnonymousUser(dto.getNickname(), dto.getPassword());
        Board board = boardValidationService.validateBoard(dto.getBoardId());

        Comment comment = CommentMapper.toEntityWithAnonymous(dto, board);

        board.addComment(comment);
        boardRepository.save(board);

        cacheService.put(board.getId(), CacheEventType.POST_PUT);
        return CommentMapper.toDto(comment);
    }

    public void deleteCommentForAnonymous(Long commentId, CommentPasswordRequestDto dto){
        boardValidationService.validateCommentPassword(commentId, dto.getPassword());

        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        board.removeComment(comment);
        boardRepository.deleteById(commentId);
        boardRepository.save(board);

        cacheService.put(board.getId(), CacheEventType.POST_PUT);
    }
}
