package com.example.crud.application.app_service.user;

import com.example.crud.application.app_service.validation.UserValidationService;
import com.example.crud.application.dto.user.CurrentPasswordRequestDto;
import com.example.crud.application.dto.user.UpdateRequestDto;
import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.UserMapper;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;

    //read
    public UserResponseDto readUser(Long userId){
        userValidationService.validateUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toDto(user);
    }

    //update
    @Transactional
    public UserResponseDto updateUser(
            UpdateRequestDto dto,
            Long userId){
        userValidationService.validateUser(userId);
        userValidationService.validatePassword(userId, dto.getCurrentPassword());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        user.update(dto.getName(), encodedPassword);
        return UserMapper.toDto(user);
    }

    //delete
    @Transactional
    public void deleteUser(Long userId, CurrentPasswordRequestDto currentPasswordRequestDto){
        userValidationService.validateUser(userId);

        userValidationService.validatePassword(userId, currentPasswordRequestDto.getCurrentPassword());

        boardRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    //get(for cookie)
    public Long getIdByEmail(String email){
        return userRepository.findIdByEmail(email);
    }
}
