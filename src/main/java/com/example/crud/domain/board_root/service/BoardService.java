package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardReadResponseDto;
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
import com.example.crud.domain.board_root.valueobjects.Category;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.service.UserValidationService;
import com.example.crud.infrastructure.cache.CustomCacheable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService extends AbstractBoardService{

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserValidationService userValidationService;
    private final BoardValidationService boardValidationService;
    private final BoardAsyncService boardAsyncService;
    private final BoardPagingService boardPagingService;

    @Override
    protected void validateUser(Object userInfo){
        HttpServletRequest req = (HttpServletRequest) userInfo;
        userValidationService.validateUser(req);
    }

    @Override
    protected void validateUserForDelete(Object userInfo, Long id){
        boardValidationService.validateBoard(id);
        HttpServletRequest req = (HttpServletRequest) userInfo;
        userValidationService.validateUser(req);
    }

    //createPost
    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        HttpServletRequest req = (HttpServletRequest) userInfo;
        User sessionUser = (User) req.getSession().getAttribute("user");
        Board board = BoardMapper.toEntity(dto, sessionUser);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //updatePost
    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id){
        Board board = boardValidationService.validateBoard(id);

        board.updatePost(dto.getContent(), dto.getTitle(), Category.valueOf(dto.getCategory()));

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long id){
        boardRepository.deleteById(id);
    }

    //readPost
    @Transactional
    @CustomCacheable(key = "'post::' + #id")
    public BoardReadResponseDto readPost(Long id, int page, int size){
        Board board = boardValidationService.validateBoard(id);
        boardRepository.save(board);

        boardAsyncService.updateViewCountAsync(board);
        Page<CommentResponseDto> comments = boardPagingService.pagingComments(id, page, size);
        return BoardMapper.toReadDto(board, comments);
    }

    //likePost
    @CustomCacheable(key = "'post::' + #id")
    public BoardResponseDto likePost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        board.updateLiked(board.getLiked() + 1);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //createComment
    @CustomCacheable(key = "'post::' + #id")
    public CommentResponseDto createComment(HttpServletRequest req, CommentRequestDto dto) {
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        User user = userValidationService.validateUser(req);

        Comment comment = CommentMapper.toEntity(dto, user, board);

        board.addComment(comment);
        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    @CustomCacheable(key = "'post::' + #id")
    public void deleteComment(HttpServletRequest req, CommentPasswordRequestDto dto, Long commentId) {
        userValidationService.validateUser(req);
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }
}
