package com.example.crud.controller.comment.dto;

import com.example.crud.controller.comment.entity.CommentDb;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    //entity -> dto
    public static CommentResponseDto toCommentResponseDto(CommentDb commentDb){
        return CommentResponseDto.builder()
                .id(commentDb.getId())
                .nickname(commentDb.getNickname())
                .content(commentDb.getContent())
                .createdDate(commentDb.getCreateDate())
                .bigCommentId(commentDb.getBigComment() != null ? commentDb.getBigComment().getId() : null)
                .smallComment(commentDb.getSmallComment() != null
                        ? commentDb.getSmallComment().stream()
                        .map(CommentMapper::toCommentResponseDto)
                        .collect(Collectors.toList())
                        : List.of()
                )
                .build();
    }

    //dto -> entity
    public static CommentDb fromCommentRequestDto(CommentRequestDto commentRequestDto){
        return CommentDb.builder()
                .nickname(commentRequestDto.getNickname())
                .content(commentRequestDto.getContent().getContent())
                .password(commentRequestDto.getPassword())
                .board(commentRequestDto.getBoardDb())
                .build();
    }

}
