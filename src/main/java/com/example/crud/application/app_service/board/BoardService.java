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

    //TODO: DDD와 mapper클래스는 안어울리는거같다. -> 생성자
    //TODO: User entity는 어쩔수없이 불러와야하는거같은데 전부 repository로 처리해야하나? ex) user.getName() -> 맞음

    //TODO 시영튜터님
    //TODO: 질문: mandatory + notsupported 를 함께사용할 방법이 없을까 고민하다가 이건그냥 readonly true랑 비슷하다는 결론을 냈는데
    //TODO:      사실 readonly true를 사용할거면 notsupported를 사용할거같아서. readonly true가 꼭 필요한 상황은 뭘까?
    //TODO:      짧은 견해로는 readonly true가 딱히 필요한 기능은 아닌 것 같다. 성능적으로 뛰어난 notsupported를 사용하는게?

    //TODO: 도메인 숙제 내달라.

    //createPost
    @Override
    @Transactional
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
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        board.update(dto.getTitle(), dto.getContent(), Category.valueOf(dto.getCategory()));

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //deletePost
    @Override
    @Transactional
    protected void executeDeletePost(Long id){
        boardRepository.deleteById(id);
    }

    //readPost
    @Transactional(readOnly = true)
    public BoardReadResponseDto readPost(Long id, int page, int size){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        boardAsyncService.updateViewCountAsync(id);

        Page<CommentResponseDto> comments = boardPagingService.pagingComments(id, page, size);
        return BoardMapper.toReadDto(board, comments);
    }

    //likePost
    @Transactional
    public BoardResponseDto likePost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        board.updateLiked();
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //createComment
    @Transactional
    public CommentResponseDto createComment(Long userId, CommentRequestDto dto) {
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = boardDomainService.createComment(user, user.getName(), dto.getContent(), board);

        commentRepository.save(comment);
        boardRepository.save(board);
        return CommentMapper.toDto(comment);
    }

    //deleteComment
    @Transactional
    public void deleteComment(CommentPasswordRequestDto dto, Long commentId, Long userId) {
        userValidationService.validateUser(userId);

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        board.removeComment(comment);
        commentRepository.deleteById(commentId);
        boardRepository.save(board);
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
}
