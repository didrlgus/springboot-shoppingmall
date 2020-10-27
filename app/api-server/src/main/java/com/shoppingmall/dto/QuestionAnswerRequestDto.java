package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class QuestionAnswerRequestDto {

    private Long userId;
    @NotBlank(message = "댓글 작성해주세요.")
    @Size(max = 255, message = "댓글 사이즈를 초과하였습니다.")
    private String message;
}
