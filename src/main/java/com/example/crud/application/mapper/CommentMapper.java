package com.example.crud.application.mapper;

import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.entities.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    //entity -> dto
    public static CommentResponseDto toDto(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .bigCommentId(comment.getParentsComment() != null ? comment.getParentsComment().getId() : null)
                .smallComment(comment.getChildComment() != null
                        ? comment.getChildComment().stream()
                        .map(CommentMapper::toDto)
                        .collect(Collectors.toList())
                        : List.of()
                )
                .build();
    }

}
