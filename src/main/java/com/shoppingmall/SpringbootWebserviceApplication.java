package com.shoppingmall;

import com.shoppingmall.common.FileUploadProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@EnableConfigurationProperties({FileUploadProperties.class})
@SpringBootApplication
public class SpringbootWebserviceApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-social-set1.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-social-set2.yml";
            /*+ "C:\\config\\real-application.yml,"
            + "C:\\config\\local-social.yml,"
            + "C:\\config\\real-social-set1.yml,"
            + "C:\\config\\real-social-set2.yml";*/

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootWebserviceApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
