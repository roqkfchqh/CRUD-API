package com.example.crud.controller.comment.service;

import com.example.crud.controller.comment.dto.CommentMapper;
import com.example.crud.controller.comment.dto.CommentRequestDto;
import com.example.crud.controller.comment.dto.CommentResponseDto;
import com.example.crud.controller.comment.entity.CommentDb;
import com.example.crud.controller.comment.repository.CommentRepository;
import com.example.crud.controller.common.exception.BadInputException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    //create
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        CommentDb commentDb = CommentMapper.fromCommentRequestDto(commentRequestDto);
        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //read
    public List<CommentResponseDto> findAll() {
        List<CommentDb> commentDbs = commentRepository.findAll();
        return commentDbs.stream().map(CommentMapper::toCommentResponseDto).collect(Collectors.toList());
    }

    //update
    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto) {
        CommentDb commentDb = commentRepository.findById(id)
                .orElseThrow(() -> new BadInputException("없는디"));

        commentDb.setContent(commentRequestDto.getContent().getContent());
        commentDb.setNickname(commentRequestDto.getNickname());

        commentRepository.save(commentDb);
        return CommentMapper.toCommentResponseDto(commentDb);
    }

    //delete
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
