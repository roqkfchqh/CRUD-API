package com.example.crud.application.app_service.user;

import com.example.crud.application.app_service.validation.UserValidationService;
import com.example.crud.application.dto.user.SignupRequestDto;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSignupService {

    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;

    public Long registerUser(SignupRequestDto dto){
        userValidationService.isEmailTaken(dto.getEmail());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.create(dto.getName(), dto.getEmail(), encodedPassword);
        userRepository.save(user);
        return user.getId();
    }
}
