package com.example.crud.application.dto.comment;

import com.example.crud.domain.board_root.entities.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long bigCommentId;
    private List<CommentResponseDto> smallComment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    private CommentResponseDto(Comment comment, List<CommentResponseDto> smallCommentDto){
        this.id = comment.getId();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.createdDate = comment.getCreatedAt();
        this.bigCommentId = comment.getParentsComment() != null ? comment.getParentsComment().getId() : null;
        this.smallComment = smallCommentDto;
    }
}
