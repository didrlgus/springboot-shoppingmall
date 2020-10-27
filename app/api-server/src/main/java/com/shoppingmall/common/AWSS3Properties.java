package com.shoppingmall.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "custom.s3")
public class AWSS3Properties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
    private String imgUploadPath;
}
