package com.shoppingmall.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("custom.imp")
public class ImpProperties {
    private String key;
    private String secret;
}
