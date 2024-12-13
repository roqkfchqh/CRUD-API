package com.example.crud.application.exception.handler;

import com.example.crud.application.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //illegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    //validException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e){
        Map<String, String> errorMessages = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors()){
            errorMessages.put(error.getField(), error.getDefaultMessage());
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "validation exception", errorMessages);
    }

    //baseException
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException e){
        return buildErrorResponse(e.getStatus(), e.getMessage(), null);
    }

    //공통 errorResponse
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, Object errors){
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        if(errors != null){
            response.put("errors", errors);
        }
        return ResponseEntity.status(status).body(response);
    }
}
