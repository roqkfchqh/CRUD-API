package com.example.crud.application.app_service.validation;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserValidationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateUser(String userId) {
        if(userId == null){
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public void validatePassword(String sessionPassword, String inputPassword){
        if(!passwordEncoder.matches(inputPassword, sessionPassword)){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    public void isEmailTaken(String email){
        if(userRepository.findByEmail(email).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }
}
