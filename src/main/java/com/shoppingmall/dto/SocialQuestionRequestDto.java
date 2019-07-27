package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.SocialType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class SocialQuestionRequestDto {

    private Long socialUserId;
    private Long productId;
    private SocialType socialType;
    @NotBlank(message = "메세지를 작성해주세요.")
    private String message;

    @Setter
    @Getter
    @ToString
    public static class Update {
        @NotBlank(message = "메세지를 작성해주세요.")
        private String message;
    }
}
