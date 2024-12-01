package com.example.crud.controller.common.exception;

import com.example.crud.controller.common.exception.errorcode.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleBadInputException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(
                new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()),
                 errorCode.getStatus()
        );
    }
}
