package com.shoppingmall.dto;

import com.shoppingmall.domain.User;
import com.shoppingmall.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class QuestionAnswerResponseDto {

    private Long id;
    private User user;
    private Question question;
    private String message;
    private String createdDate;
}
