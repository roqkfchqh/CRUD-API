package com.example.crud.application.exception;

import com.example.crud.application.exception.errorcode.ErrorCode;

public class CustomException extends BaseException {

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode.getStatus());
    }

}
