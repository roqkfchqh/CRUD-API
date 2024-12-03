package com.example.crud.controller.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentPasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    private Long boardId;
}
