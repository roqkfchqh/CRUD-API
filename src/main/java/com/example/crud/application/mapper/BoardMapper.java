package com.example.crud.application.mapper;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.user_root.aggregate.User;

public class BoardMapper {

    //entity -> dto
    public static BoardResponseDto toDto(Board board){
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

    //dto -> entity
    public static Board toEntity(BoardRequestDto boardRequestDto, User user){
        return Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .category(boardRequestDto.getCategory())
                .user(user)
                .build();
    }

    //entity -> dto
    public static Board toDtoWithAnonymous(Board board){
        return null;
    }

    //dto -> entity
    public static Board toEntityWithAnonymous(BoardRequestDto dto){
        return null;
    }

}
