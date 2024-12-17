package com.example.crud.application.mapper;

import com.example.crud.application.dto.user.UserResponseDto;
import com.example.crud.domain.user_root.aggregate.User;

public class UserMapper {

    //entity -> dto
    public static UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
