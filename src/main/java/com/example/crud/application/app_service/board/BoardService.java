package com.example.crud.application.app_service.board;

import com.example.crud.application.app_service.validation.BoardValidationService;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.application.app_service.validation.UserValidationService;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.domain_service.BoardDomainService;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.repository.CommentRepository;
import com.example.crud.domain.board_root.valueobjects.Category;
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

    //TODO: DDD와 mapper클래스는 안어울리는거같다.
    //TODO: User entity는 어쩔수없이 불러와야하는거같은데 전부 repository로 처리해야하나? ex) user.getName()

    @Override
    protected void validateUser(Object userInfo){
        String userId = (String) userInfo;
        userValidationService.validateUser(userId);
    }

    @Override
    protected void validateUserForDelete(Object userInfo, Long id){
        boardValidationService.validateBoard(id);
        String userId = (String) userInfo;
        userValidationService.validateUser(userId);
    }

    //createPost
    @Override
    protected BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo){
        String userId = (String) userInfo;
        Board board = boardDomainService.createBoard(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()), user.getName());

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //updatePost
    @Override
    protected BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id){
        Board board = boardValidationService.validateBoard(id);
        boardDomainService.updateBoard(board, dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()));

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long id){
        boardRepository.deleteById(id);
    }

    //readPost
    public BoardReadResponseDto readPost(Long id, int page, int size){
        Board board = boardValidationService.validateBoard(id);
        boardAsyncService.updateViewCountAsync(board);

        Page<CommentResponseDto> comments = boardPagingService.pagingComments(id, page, size);
        return BoardMapper.toReadDto(board, comments);
    }

    //likePost
    public BoardResponseDto likePost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        boardDomainService.likeBoard(board);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //createComment
    public CommentResponseDto createComment(String userId, CommentRequestDto dto) {
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardDomainService.createComment(user.getName(), dto.getContent(), board);

        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    public void deleteComment(CommentPasswordRequestDto dto, Long commentId) {
        Board board = boardValidationService.validateBoard(dto.getBoardId());
        Comment comment = boardValidationService.validateComment(commentId, board);

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
    }
}
