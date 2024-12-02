package com.example.crud.controller.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCombinedRequestDto {

    private CommentRequestDto commentRequestDto;
    private CommentPasswordRequestDto commentPasswordRequestDto;
}
