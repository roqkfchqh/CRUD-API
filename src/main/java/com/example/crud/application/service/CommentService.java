package com.example.crud.application.service;

import com.example.crud.application.dto.CommentCombinedRequestDto;
import com.example.crud.application.dto.CommentPasswordRequestDto;
import com.example.crud.application.dto.CommentResponseDto;
import com.example.crud.application.mapper.CommentMapper;
import com.example.crud.domain.model.entities.BoardDb;
import com.example.crud.domain.repository.BoardRepository;
import com.example.crud.domain.model.entities.CommentDb;
import com.example.crud.domain.repository.CommentRepository;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.ErrorCode;
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
    @CachePut(value = "comments", key = "'post: ' + #boardId", unless = "#result == null")
    public CommentResponseDto createComment(Long boardId, CommentCombinedRequestDto commentCombinedRequestDto) {
        BoardDb boardDb = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(commentCombinedRequestDto.getCommentPasswordRequestDto().getPassword());
        CommentDb commentDb = CommentMapper.fromCommentRequestDto(commentCombinedRequestDto);
        commentDb.setPassword(encodedPassword);
        commentDb.setBoard(boardDb);
        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //get
    @Cacheable(value = "comments", key = "'post: ' + #boardDb.getId()", unless="#result == null")
    public Page<CommentResponseDto> getCommentsBoard(BoardDb boardDb, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<CommentDb> commentsPage = commentRepository.findByBoard(boardDb, pageable);

        return commentsPage.map(CommentMapper::toCommentResponseDto);
    }

    //update
    @CachePut(value = "comments", key = "'post: ' + #boardId", unless = "#result == null")
    public CommentResponseDto updateComment(Long boardId, Long id, CommentCombinedRequestDto commentCombinedRequestDto) {
        CommentDb commentDb = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(commentCombinedRequestDto.getCommentPasswordRequestDto().getPassword(), commentDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        commentDb.updateContent(commentCombinedRequestDto.getCommentRequestDto().getContent());

        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //delete
    @CachePut(value = "comments", key = "'post: ' + #boardId", unless = "#result == null")
    public void deleteComment(Long boardId, Long id, CommentPasswordRequestDto commentPasswordRequestDto) {
        CommentDb commentDb = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(commentPasswordRequestDto.getPassword(), commentDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        commentRepository.deleteById(id);
    }
}
