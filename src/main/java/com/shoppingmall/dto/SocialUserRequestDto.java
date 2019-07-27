package com.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class SocialUserRequestDto {

    @Size(max = 50, message = "도로명 주소가 적절하지 않습니다.")
    private String roadAddr;
    @Size(max = 50, message = "건물이름이 적절하지 않습니다.")
    private String buildingName;
    @Size(max = 50, message = "상세주소가 적절하지 않습니다.")
    private String detailAddr;

}
