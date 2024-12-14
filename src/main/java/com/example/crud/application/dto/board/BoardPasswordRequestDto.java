package com.example.crud.application.dto.board;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardPasswordRequestDto {

    @Nullable
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
