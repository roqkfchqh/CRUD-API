package com.example.crud.controller.board.service.crud;

import com.example.crud.controller.common.exception.BadInputException;
import org.springframework.security.crypto.bcrypt.BCrypt;

public abstract class PasswordRequiredService{
    public void withPassword(String inputPassword, String realPassword){
        if(!BCrypt.checkpw(inputPassword, realPassword)){
            throw new BadInputException("비밀번호가 일치하지 않습니다");
        }
        performAction();
    }
    protected abstract void performAction();
}