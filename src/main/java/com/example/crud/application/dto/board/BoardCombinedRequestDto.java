package com.example.crud.application.dto.board;

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
