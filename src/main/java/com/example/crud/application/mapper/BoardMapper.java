package com.example.crud.application.mapper;

import com.example.crud.application.dto.board.BoardSearchPagingResponseDto;
import com.example.crud.application.dto.board.BoardReadResponseDto;
import com.example.crud.application.dto.board.BoardResponseDto;

import com.example.crud.application.dto.comment.CommentResponseDto;
import com.example.crud.domain.board_root.aggregate.Board;
import org.springframework.data.domain.Page;

public class BoardMapper {

    private final static int HOT_TITLE = 10;

    //entity -> dto
    public static BoardResponseDto toDto(Board board){
        String hotTitle = getHotTitle(board);
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .content(board.getContent())
                .nickname(board.getNickname())
                .category(board.getCategory().toString())
                .liked(board.getLiked())
                .count(board.getCount())
                .createdAt(board.getCreatedAt())
                .build();
    }

    //entity -> dto (read)
    public static BoardReadResponseDto toReadDto(Board board, Page<CommentResponseDto> comments){
        String hotTitle = getHotTitle(board);
        return BoardReadResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .content(board.getContent())
                .nickname(board.getNickname())
                .category(board.getCategory().toString())
                .liked(board.getLiked())
                .count(board.getCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .comments(comments)
                .build();
    }

    //entity -> dto (paging)
    public static BoardSearchPagingResponseDto toPagingDto(Board board){
        String hotTitle = getHotTitle(board);
        return BoardSearchPagingResponseDto.builder()
                .id(board.getId())
                .title(hotTitle)
                .nickname(board.getNickname())
                .category(board.getCategory().toString())
                .liked(board.getLiked())
                .count(board.getCount())
                .createdAt(board.getCreatedAt())
                .commentsNum((long) board.getComments().size())
                .build();
    }

    private static String getHotTitle(Board board) {
        return board.getLiked() > HOT_TITLE ? "\uD83D\uDD25 " + board.getTitle() : board.getTitle();
    }

}
