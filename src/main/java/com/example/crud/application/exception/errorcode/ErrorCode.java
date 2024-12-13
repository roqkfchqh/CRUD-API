package com.example.crud.application.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    PAGING_ERROR(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다."),
    ALREADY_USED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 잘못 입력하였습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),

    //401
    WRONG_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일이나 비밀번호를 잘못 입력하였습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    //403
    FORBIDDEN_OPERATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    //404
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 페이지입니다."),

    //429
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "30초에 한번만 글을 쓸 수 있습니다."),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 관리를 못해서 줴송합니다.."),

    //502
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "잘못된 접근입니다.");

    private final HttpStatus status;
    private final String message;
}
