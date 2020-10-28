package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class MeRequestDto {

    @NotBlank(message = "이름을 작성해주세요.")
    @Size(min = 1, max = 10, message = "이름을 알맞게 작성해주세요.")
    private String name;

    @NotBlank(message = "이메일을 작성해주세요.")
    @Email(message = "이메일의 양식을 지켜주세요.")
    private String email;

    @Size(max = 50, message = "도로명 주소를 알맞게 작성해주세요.")
    private String roadAddr;

    @Size(max = 50, message = "건물 이름을 알맞게 작성해주세요.")
    private String buildingName;

    @Size(max = 20, message = "상세주소를 알맞게 작성해주세요.")
    private String detailAddr;
}
