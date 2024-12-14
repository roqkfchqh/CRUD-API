package com.example.crud.application.mapper;

import com.example.crud.application.dto.comment.CommentRequestDto;
import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.user_root.aggregate.User;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    //entity -> dto
    public static CommentResponseDto toDto(Comment comment){
        return CommentResponseDto.builder()
                .id(comment.getId())
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .createdDate(comment.getCreatedAt())
                .bigCommentId(comment.getParentsComment() != null ? comment.getParentsComment().getId() : null)
                .smallComment(comment.getChildComment() != null
                        ? comment.getChildComment().stream()
                        .map(CommentMapper::toDto)
                        .collect(Collectors.toList())
                        : List.of()
                )
                .build();
    }

    //dto -> entity
    public static Comment toEntity(CommentRequestDto dto, User user, Board board){
        return Comment.builder()
                .nickname(user.getName())
                .content(dto.getContent())
                .board(board)
                .build();
    }

    //dto -> entity
    public static Comment toEntityWithAnonymous(CommentRequestDto dto, Board board){
        return Comment.builder()
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .content(dto.getContent())
                .board(board)
                .build();
    }

}
