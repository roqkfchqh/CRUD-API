package com.example.crud.application.app_service.validation;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void validateUser(Long userId) {
        if(userId == null){
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void validatePassword(Long id, String inputPassword){
        if(!passwordEncoder.matches(inputPassword, userRepository.findPasswordById(id))){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void isEmailTaken(String email){
        if(userRepository.findByEmail(email).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }
}
