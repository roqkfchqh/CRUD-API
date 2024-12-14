package com.example.crud.application.mapper;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.user_root.aggregate.User;

public class BoardMapper {

    private final static int HOT_TITLE = 10;

    //entity -> dto
    public static BoardResponseDto toDto(Board board){
        String hotTitle = board.getLiked() > HOT_TITLE ? "\uD83D\uDD25 " + board.getTitle() : board.getTitle();
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .content(board.getContent())
                .nickname(board.getNickname())
                .category(board.getCategory())
                .liked(board.getLiked())
                .count(board.getCount())
                .createDate(board.getCreatedAt())
                .build();
    }

    //dto -> entity
    public static Board toEntity(BoardRequestDto dto, User user){
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .nickname(user.getName())
                .user(user)
                .build();
    }

    //dto -> entity
    public static Board toEntityWithAnonymous(BoardRequestDto dto, String encodedPassword){
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .nickname(dto.getNickname())
                .password(encodedPassword)
                .build();
    }

}
