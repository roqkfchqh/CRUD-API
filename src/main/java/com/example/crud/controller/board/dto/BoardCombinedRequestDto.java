package com.example.crud.controller.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCombinedRequestDto {

    private BoardRequestDto boardRequestDto;
    private BoardPasswordRequestDto boardPasswordRequestDto;
}
