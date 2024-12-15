package com.example.crud.application.mapper;

import com.example.crud.application.dto.board.BoardPagingResponseDto;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;

import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.user_root.aggregate.User;
import org.springframework.data.domain.Page;

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
                .createdAt(board.getCreatedAt())
                .build();
    }

    //entity -> dto (read)
    public static BoardReadResponseDto toReadDto(Board board, Page<CommentResponseDto> comments){
        String hotTitle = board.getLiked() > HOT_TITLE ? "\uD83D\uDD25 " + board.getTitle() : board.getTitle();
        return BoardReadResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .content(board.getContent())
                .nickname(board.getNickname())
                .category(board.getCategory())
                .liked(board.getLiked())
                .count(board.getCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .comments(comments)
                .build();
    }

    //entity -> dto (paging)
    public static BoardPagingResponseDto toPagingDto(Board board){
        String hotTitle = board.getLiked() > HOT_TITLE ? "\uD83D\uDD25 " + board.getTitle() : board.getTitle();
        return BoardPagingResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .nickname(board.getNickname())
                .category(board.getCategory())
                .liked(board.getLiked())
                .count(board.getCount())
                .createdAt(board.getCreatedAt())
                .commentsNum((long) board.getComments().size())
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

    //dto -> entity (anonymous)
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
