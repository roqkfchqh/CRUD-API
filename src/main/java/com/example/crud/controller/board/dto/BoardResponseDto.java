package com.example.crud.controller.board.dto;

import com.example.crud.controller.board.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Category category;
    private String password;
    private int liked;
    private int count;
    private LocalDateTime createDate;
}
