package com.example.crud.application.dto.board;

import com.example.crud.domain.board_root.valueobjects.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 작성 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private Category category;
}
