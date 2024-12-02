package com.example.crud.controller.comment.dto;

import com.example.crud.controller.board.entity.BoardDb;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 200, message = "너무길자나지금")
    private String content;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로만 가능합니다.")
    private String nickname;

    @NotNull(message = "잘못된 접근입니다.")
    private BoardDb boardDb;
}
