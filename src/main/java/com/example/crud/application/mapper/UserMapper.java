package com.example.crud.application.mapper;

import com.example.crud.application.dto.user.SignupRequestDto;
import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.domain.user_root.aggregate.User;

public class UserMapper {

    //entity -> dto
    public static UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt().toString())
                .build();
    }

    //signupDto -> entity
    public static User toEntity(SignupRequestDto dto, String encodedPassword){
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .build();
    }
}
