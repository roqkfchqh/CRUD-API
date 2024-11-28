package com.example.crud.controller.common.exception;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}
