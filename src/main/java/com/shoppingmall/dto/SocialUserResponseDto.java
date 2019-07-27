package com.shoppingmall.dto;

import com.shoppingmall.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SocialUserResponseDto {

    private String name;
    private String email;
    private SocialType socialType;

}
