package com.example.crud.application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private Long boardId;
    //private Long parentCommentId;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 100, message = "너무길자나지금")
    private String content;

    //비로그인 사용자만
    private String nickname;
    private String password;
}
