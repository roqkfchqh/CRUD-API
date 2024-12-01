package com.example.crud.controller.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하였습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "404 에러: 없는 페이지입니다."),
    FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "429 에러: 천천히 하세요"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500 에러: 서버 관리를 못해서 줴송합니다.."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "502 에러: 잘못된 접근입니다.");


    //USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다. 회원가입이 필요합니다."),
    //USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    //LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;





}
