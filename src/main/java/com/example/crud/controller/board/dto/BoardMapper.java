package com.example.crud.controller.board.dto;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;

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
                .password(boardRequestDto.getPassword())
                .category(Category.valueOf(boardRequestDto.getCategory().toUpperCase()))
                .build();
    }

}
