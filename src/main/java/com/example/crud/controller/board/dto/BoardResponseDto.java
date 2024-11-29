package com.example.crud.controller.board.dto;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Category category;
    private int liked;
    private int count;
    private LocalDateTime createDate;

    public BoardResponseDto(BoardDb boardDb) {
        this.id = boardDb.getId();
        this.title = boardDb.getTitle();
        this.content = boardDb.getContent();
        this.nickname = boardDb.getNickname();
        this.category = boardDb.getCategory();
        this.liked = boardDb.getLiked();
        this.count = boardDb.getCount();
        this.createDate = boardDb.getCreatedDate();
    }
}
