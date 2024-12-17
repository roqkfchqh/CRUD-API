package com.example.crud.application.app_service.user;

import com.example.crud.application.app_service.validation.UserValidationService;
import com.example.crud.application.dto.user.CurrentPasswordRequestDto;
import com.example.crud.application.dto.user.UpdateRequestDto;
import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.UserMapper;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.user_root.repository.UserRepository;
import com.example.crud.domain.user_root.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    private final UserDomainService userDomainService;

    //read
    public UserResponseDto readUser(User user){
        userValidationService.validateUser(user);
        return UserMapper.toDto(user);
    }

    //update
    public UserResponseDto updateUser(
            UpdateRequestDto dto,
            User user){

        userValidationService.validateUser(user);
        userValidationService.validatePassword(user.getPassword(), dto.getCurrentPassword());

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        userDomainService.updateUser(user, dto.getName(), encodedPassword);
        return UserMapper.toDto(user);
    }

    //delete
    public void deleteUser(User user, CurrentPasswordRequestDto currentPasswordRequestDto){
        userValidationService.validateUser(user);
        userValidationService.validatePassword(user.getPassword(), currentPasswordRequestDto.getCurrentPassword());
        boardRepository.deleteByUserId(user.getId());
        userRepository.deleteById(user.getId());
    }

    //get(for cookie)
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
