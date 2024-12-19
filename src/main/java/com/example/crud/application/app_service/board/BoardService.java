package com.example.crud.application.app_service.board;

import com.example.crud.application.app_service.validation.BoardValidationService;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.application.app_service.validation.UserValidationService;
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
public class BoardService extends AbstractBoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserValidationService userValidationService;
    private final BoardValidationService boardValidationService;
    private final BoardAsyncService boardAsyncService;
    private final BoardPagingService boardPagingService;
    private final BoardDomainService boardDomainService;
    private final UserRepository userRepository;

    //createPost
    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        Long userId = (Long) userInfo;
        String userName = userRepository.findNameById(userId);

        Board board = Board.create(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()), userName);

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //updatePost
    @Override
    @Transactional
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id){
        Board board = getBoard(id);

        board.update(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()));

        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long id){
        boardRepository.deleteById(id);
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

    //createComment
    @Transactional
    public CommentResponseDto createComment(Long userId, CommentRequestDto dto) {
        Board board = getBoard(dto.getBoardId());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = boardDomainService.createComment(user, user.getName(), dto.getContent(), board);

        commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    @Transactional
    public void deleteComment(CommentPasswordRequestDto dto, Long commentId, Long userId) {
        userValidationService.validateUser(userId);

        Board board = getBoard(dto.getBoardId());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
    }

    @Override
    protected void validateUser(Object userInfo){
        Long userId = (Long) userInfo;
        userValidationService.validateUser(userId);
    }

    @Override
    protected void validateUserForDelete(Object userInfo, Long id){
        boardValidationService.validateBoard(id);
        Long userId = (Long) userInfo;
        userValidationService.validateUser(userId);
    }

    @Override
    protected void validateUserForUpdate(Object userInfo, Long id){
        boardValidationService.validateBoard(id);
        Long userId = (Long) userInfo;
        userValidationService.validateUser(userId);
    }

    private Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}
