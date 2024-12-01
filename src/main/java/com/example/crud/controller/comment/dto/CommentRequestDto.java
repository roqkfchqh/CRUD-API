package com.example.crud.controller.comment.dto;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.comment.entity.CommentDb;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 200, message = "너무길자나지금")
    private String content;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로만 가능합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하로 가능합니다.")
    private String password;

    @NotNull(message = "잘못된 접근입니다.")
    private BoardDb boardDb;
}
