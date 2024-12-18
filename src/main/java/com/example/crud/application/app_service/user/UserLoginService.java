package com.example.crud.application.app_service.user;

import com.example.crud.application.dto.user.LoginRequestDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long loginUser(LoginRequestDto dto){
        Long userId = userRepository.findIdByEmail(dto.getEmail());
        if(userId == null || !passwordEncoder.matches(dto.getPassword(), userRepository.findPasswordById(userId))){
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
        return userId;
    }
}