package com.example.crud.application.dto.comment;

import com.example.crud.domain.board_root.entities.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private Long bigCommentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.bigCommentId = comment.getParentsComment() != null ? comment.getParentsComment().getId() : null;
    }
}
