package com.shoppingmall.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class UpdatePasswordRequestDto {

    @NotBlank(message = "기존 비밀번호를 작성하세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9`~!@#$%^&*()\\-_+=\\\\]).{8,15}$", message = "비밀번호는 영문/숫자/특수문자 조합 8자리~15자리로 작성하세요.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호를 작성하세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9`~!@#$%^&*()\\-_+=\\\\]).{8,15}$", message = "비밀번호는 영문/숫자/특수문자 조합 8자리~15자리로 작성하세요.")
    private String newPassword;
}
