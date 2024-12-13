package com.example.crud.application.mapper;

import com.example.crud.application.dto.BoardRequestDto;
import com.example.crud.application.dto.BoardResponseDto;
import com.example.crud.domain.model.entities.BoardDb;
import com.example.crud.domain.model.valueobjects.Category;

public class BoardMapper {

    //엔티티 -> DTO
    public static BoardResponseDto toResponseDto(BoardDb boardDb){
        String hotTitle = boardDb.getLiked() > 10 ? "\uD83D\uDD25 " + boardDb.getTitle() : boardDb.getTitle();
        return BoardResponseDto.builder()
                .id(boardDb.getId())
                .title(hotTitle)
                .content(boardDb.getContent())
                .nickname(boardDb.getNickname())
                .category(boardDb.getCategory())
                .password(boardDb.getPassword())
                .liked(boardDb.getLiked())
                .count(boardDb.getCount())
                .createDate(boardDb.getCreatedDate())
                .build();
    }

    //DTO -> 엔티티
    public static BoardDb fromRequestDto(BoardRequestDto boardRequestDto){
        return BoardDb.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .nickname(boardRequestDto.getNickname())
                .category(Category.valueOf(boardRequestDto.getCategory().toUpperCase()))
                .build();
    }

}
