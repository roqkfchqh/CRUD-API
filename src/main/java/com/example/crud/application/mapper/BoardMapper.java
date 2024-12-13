package com.example.crud.application.mapper;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.valueobjects.Category;

public class BoardMapper {

    //엔티티 -> DTO
    public static BoardResponseDto toResponseDto(Board board){
        String hotTitle = board.getLiked() > 10 ? "\uD83D\uDD25 " + board.getTitle() : board.getTitle();
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .content(board.getContent())
                .nickname(board.getNickname())
                .category(board.getCategory())
                .password(board.getPassword())
                .liked(board.getLiked())
                .count(board.getCount())
                .createDate(board.getCreatedAt())
                .build();
    }

    //DTO -> 엔티티
    public static Board fromRequestDto(BoardRequestDto boardRequestDto){
        return Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .nickname(boardRequestDto.getNickname())
                .category(Category.valueOf(boardRequestDto.getCategory().toUpperCase()))
                .build();
    }

}
