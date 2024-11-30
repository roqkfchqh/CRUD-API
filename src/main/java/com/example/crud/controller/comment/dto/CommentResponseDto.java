package com.example.crud.controller.comment.dto;

import com.example.crud.controller.comment.entity.CommentDb;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdDate;
    private Long bigCommentId;
    private List<CommentResponseDto> smallComment;

    private CommentResponseDto(CommentDb commentDb, List<CommentResponseDto> smallCommentDto){
        this.id = commentDb.getId();
        this.nickname = commentDb.getNickname();
        this.content = commentDb.getContent();
        this.createdDate = commentDb.getCreateDate();
        this.bigCommentId = commentDb.getBigComment() != null ? commentDb.getBigComment().getId() : null;
        this.smallComment = smallCommentDto;
    }
}
