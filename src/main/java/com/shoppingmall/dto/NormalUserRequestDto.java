package com.shoppingmall.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class NormalUserRequestDto {

    private String identifier;
    private String password;
    private String name;
    private String email;
    private String authorities;
}

