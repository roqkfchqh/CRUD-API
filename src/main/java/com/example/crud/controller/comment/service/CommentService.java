package com.example.crud.controller.comment.service;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.comment.dto.CommentMapper;
import com.example.crud.controller.comment.dto.CommentRequestDto;
import com.example.crud.controller.comment.dto.CommentResponseDto;
import com.example.crud.controller.comment.entity.CommentDb;
import com.example.crud.controller.comment.repository.CommentRepository;
import com.example.crud.controller.common.exception.CustomException;
import com.example.crud.controller.common.exception.ErrorCode;
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

    //create
    @CachePut(value = "comments", key = "'Post: ' + #boardId")
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto) {
        String encodedPassword = passwordEncoder.encode(commentRequestDto.getPassword());
        CommentDb commentDb = CommentMapper.fromCommentRequestDto(commentRequestDto);
        commentDb.setPassword(encodedPassword);
        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //get
    @Cacheable(value = "comments", key = "'Post: ' + #boardDb.getId()")
    public Page<CommentResponseDto> getCommentsBoard(BoardDb boardDb, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<CommentDb> commentsPage = commentRepository.findByBoard(boardDb, pageable);

        return commentsPage.map(CommentMapper::toCommentResponseDto);
    }

    //update
    @CachePut(value = "comments", key = "'Post: ' + #boardId")
    public CommentResponseDto updateComment(Long boardId, Long id, CommentRequestDto commentRequestDto) {
        CommentDb commentDb = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(commentRequestDto.getPassword(), commentDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        commentDb.updateContent(commentRequestDto.getContent());

        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //delete
    @CachePut(value = "comments", key = "'Post: ' + #boardId")
    public void deleteComment(Long boardId, Long id, String password) {
        CommentDb commentDb = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(password, commentDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        commentRepository.deleteById(id);
    }
}
