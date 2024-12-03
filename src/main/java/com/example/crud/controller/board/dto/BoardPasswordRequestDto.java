package com.example.crud.controller.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardPasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}