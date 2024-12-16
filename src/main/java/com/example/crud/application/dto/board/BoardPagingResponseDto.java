package com.example.crud.application.dto.board;

import com.example.crud.domain.board_root.valueobjects.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class BoardPagingResponseDto {

    private Long id;
    private String title;
    private String nickname;
    private Category category;
    private Long commentsNum;
    private int liked;
    private int count;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
