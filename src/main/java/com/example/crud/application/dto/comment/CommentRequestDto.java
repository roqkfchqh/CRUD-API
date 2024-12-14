package com.example.crud.application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 200, message = "너무길자나지금")
    private String content;
}
