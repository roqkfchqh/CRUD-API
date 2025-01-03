package com.example.crud.application.dto.board;

import lombok.Getter;

@Getter
public class AnonymousRequestDto {

    //비로그인 사용자만
    private String nickname;
    private String password;
}
