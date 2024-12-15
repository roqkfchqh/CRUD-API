package com.example.crud.domain.board_root.service;

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
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.service.UserValidationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        HttpServletRequest req = (HttpServletRequest) userInfo;
        User user = userValidationService.validateUser(req);
        Board board = BoardMapper.toEntity(dto, user);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Object userInfo, Long id){
        HttpServletRequest req = (HttpServletRequest) userInfo;
        userValidationService.validateUser(req);
        Board board = boardValidationService.validateBoard(id);

        board.updatePost(dto.getContent(), dto.getTitle(), dto.getCategory());

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    @Override
    protected void executeDeletePost(Object userInfo, Long id){
        boardRepository.deleteById(id);
    }

//    //create
//    @CacheEvict(value = "boards", allEntries = true)
//    public BoardResponseDto createPost(HttpServletRequest req, BoardRequestDto dto) {
//        User user = userValidationService.validateUser(req);
//        Board board = BoardMapper.toEntity(dto, user);
//
//        boardRepository.save(board);
//
//        return BoardMapper.toDto(board);
//    }

    //read
    @Value("${cache.view.threshold:100}")
    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#id", unless="#result == null")
    public BoardResponseDto readPost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);

        boardAsyncService.updateViewCountAsync(board);
        return BoardMapper.toDto(board);
    }

//    //update
//    @CachePut(value = "posts", key = "#id", unless = "#result == null")
//    public BoardResponseDto updatePost(HttpServletRequest req, Long id, BoardRequestDto dto) {
//        userValidationService.validateUser(req);
//        Board board = boardValidationService.validateBoard(id);
//
//        board.updatePost(
//                dto.getContent(),
//                dto.getTitle(),
//                dto.getCategory()
//        );
//
//        boardRepository.save(board);
//        return BoardMapper.toDto(board);
//    }

    //like
    @Value("${cache.view.threshold:30}")
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    public BoardResponseDto likePost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        board.updateLiked(board.getLiked() + 1);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

//    //delete
//    @CachePut(value = "posts", key = "#id", unless = "#result == null")
//    @CacheEvict(value = "boards", allEntries = true)
//    public void deletePost(HttpServletRequest req, Long id){
//        boardValidationService.validateBoard(id);
//        userValidationService.validateUser(req);
//        boardRepository.deleteById(id);
//    }

    //create
    @CachePut(value = "posts", key = "#boardId", unless = "#result == null")
    public CommentResponseDto createComment(HttpServletRequest req, CommentRequestDto dto) {
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        User user = userValidationService.validateUser(req);

        Comment comment = CommentMapper.toEntity(dto, user, board);

        board.addComment(comment);
        boardRepository.save(board);

        return CommentMapper.toDto(comment);
    }

    //delete
    @CachePut(value = "posts", key = "#boardId", unless = "#result == null")
    public void deleteComment(HttpServletRequest req, CommentPasswordRequestDto dto, Long commentId) {
        userValidationService.validateUser(req);
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }
}
