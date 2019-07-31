package com.shoppingmall.dto;

import com.shoppingmall.domain.NormalUser;
import com.shoppingmall.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class QuestionAnswerResponseDto {

    private Long id;
    private NormalUser normalUser;
    private Question question;
    private String message;
    private String createdDate;
}
