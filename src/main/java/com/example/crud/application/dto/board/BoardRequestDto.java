package com.example.crud.application.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 20, message = "제목은 1자 이상 20자 이하로 작성 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 5, max = 300, message = "내용은 5자 이상 300자 이하로 작성 가능합니다.")
    private String content;

    @NotBlank(message = "카테고리를 입력해주세요.")
    private String category;
}
