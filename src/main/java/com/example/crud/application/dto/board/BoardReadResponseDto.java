package com.example.crud.application.dto.board;

import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.valueobjects.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class BoardReadResponseDto {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Category category;
    private int liked;
    private int count;

    private Page<CommentResponseDto> comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
