package com.example.crud.application.app_service.validation;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateUser(Long userId) {
        if(userId == null){
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        if(!userRepository.existsById(userId)){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void validatePassword(Long id, String inputPassword){
        if(!passwordEncoder.matches(inputPassword, userRepository.findPasswordById(id))){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void isEmailTaken(String email){
        if(userRepository.existsByEmail(email)){
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }
}
