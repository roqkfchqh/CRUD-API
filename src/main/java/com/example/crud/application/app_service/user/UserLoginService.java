package com.example.crud.application.app_service.user;

import com.example.crud.application.dto.user.LoginRequestDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public String loginUser(LoginRequestDto dto){
        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if(user.isEmpty() || !passwordEncoder.matches(dto.getPassword(), user.get().getPassword())){
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
        return user.get();
    }
}