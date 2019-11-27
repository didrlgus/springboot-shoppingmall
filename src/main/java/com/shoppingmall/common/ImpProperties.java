package com.shoppingmall.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("custom.imp")
public class ImpProperties {
    private String key;
    private String secret;
}
