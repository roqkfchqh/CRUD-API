package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.comment.CommentPasswordRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.repository.CommentRepository;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;

    //create
    @CachePut(value = "posts", key = "#boardId", unless = "#result == null")
    public CommentResponseDto createComment(Long boardId, CommentCombinedRequestDto commentCombinedRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(commentCombinedRequestDto.getCommentPasswordRequestDto().getPassword());
        Comment comment = CommentMapper.fromCommentRequestDto(commentCombinedRequestDto);
        comment.setPassword(encodedPassword);
        comment.setBoard(board);
        commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(comment);
    }

    //get
    @Cacheable(value = "posts", key = "#board.getId()", unless="#result == null")
    public Page<CommentResponseDto> getCommentsBoard(Board board, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> commentsPage = commentRepository.findByBoard(board, pageable);

        return commentsPage.map(CommentMapper::toCommentResponseDto);
    }

    //update
    @CachePut(value = "posts", key = "#boardId", unless = "#result == null")
    public CommentResponseDto updateComment(Long boardId, Long id, CommentCombinedRequestDto commentCombinedRequestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(commentCombinedRequestDto.getCommentPasswordRequestDto().getPassword(), comment.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        comment.updateContent(commentCombinedRequestDto.getCommentRequestDto().getContent());

        commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(comment);
    }

    //delete
    @CachePut(value = "posts", key = "#boardId", unless = "#result == null")
    public void deleteComment(Long boardId, Long id, CommentPasswordRequestDto commentPasswordRequestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(commentPasswordRequestDto.getPassword(), comment.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        commentRepository.deleteById(id);
    }
}
