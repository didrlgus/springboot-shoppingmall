package com.shoppingmall.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {

    // application.yml에 등록한 properties들을 세팅 (롬복의 getter, setter)
    private String uploadDir;
    private String productUploadDir;
}
