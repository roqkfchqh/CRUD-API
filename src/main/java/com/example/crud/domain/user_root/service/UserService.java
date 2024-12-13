package com.example.crud.domain.user_root.service;

import com.example.crud.application.dto.user.CurrentPasswordRequestDto;
import com.example.crud.application.dto.user.UpdateRequestDto;
import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.application.mapper.UserMapper;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.user_root.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserValidationService userValidationService;

    //read
    public UserResponseDto readUser(HttpServletRequest req){
        User user = userValidationService.validateUser(req);
        return UserMapper.toDto(user);
    }

    //update
    public UserResponseDto updateUser(
            UpdateRequestDto dto,
            HttpServletRequest req){

        User user = userValidationService.validateUser(req);
        userValidationService.validatePassword(user.getPassword(), dto.getCurrentPassword());

        user.updateUser(dto.getName(), dto.getNewPassword());
        return UserMapper.toDto(user);
    }

    //delete
    public void deleteUser(HttpServletRequest req, CurrentPasswordRequestDto currentPasswordRequestDto){
        User user = userValidationService.validateUser(req);
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
