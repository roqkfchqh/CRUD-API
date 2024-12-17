package com.example.crud.application.app_service.board;

import com.example.crud.application.app_service.validation.BoardValidationService;
import com.example.crud.application.dto.board.BoardPasswordRequestDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.domain_service.BoardDomainService;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardAnonymousService extends AbstractBoardService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardValidationService boardValidationService;
    private final CommentRepository commentRepository;
    private final BoardDomainService boardDomainService;

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
        Board board = boardDomainService.createAnonymousBoard(dto.getTitle(), dto.getContent(), dto.getCategory(), dto.getNickname(), encodedPassword);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //updatePost
    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id){
        Board board = boardValidationService.validateBoardPassword(id, dto.getPassword());

        boardDomainService.updateBoard(board, dto.getTitle(), dto.getContent(), dto.getCategory());
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long id){
        boardRepository.deleteById(id);
    }

    //createComment
    public CommentResponseDto createCommentForAnonymous(CommentRequestDto dto){
        boardValidationService.validateAnonymousUser(dto.getNickname(), dto.getPassword());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Board board = boardValidationService.validateBoard(dto.getBoardId());

        Comment comment = boardDomainService.createAnonymousComment(dto.getNickname(), dto.getContent(), board, encodedPassword);

        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    public void deleteCommentForAnonymous(Long commentId, CommentPasswordRequestDto dto){
        boardValidationService.validateCommentPassword(commentId, dto.getPassword());

        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        boardDomainService.removeComment(board, comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }
}
