package com.example.crud.application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentPasswordRequestDto {

    private Long boardId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 3, max = 10, message = "비밀번호는 2자 이상 10자 이하로 입력해주세요.")
    private String password;
}
