package com.example.crud.application.dto.board;

import com.example.crud.domain.board_root.valueobjects.Category;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 20, message = "제목은 1자 이상 20자 이하로 작성 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 5, max = 300, message = "내용은 5자 이상 300자 이하로 작성 가능합니다.")
    private String content;

    @NotBlank(message = "카테고리를 입력해주세요.")
    private Category category;

    @Nullable
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로만 가능합니다.")
    private String nickname;

    @Nullable
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 3, max = 10, message = "비밀번호는 2자 이상 10자 이하로 입력해주세요.")
    private String password;
}
