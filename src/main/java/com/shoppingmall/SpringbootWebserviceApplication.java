package com.shoppingmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class SpringbootWebserviceApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-application.yml,";
            /*+ "/home/ec2-user/app/config/springboot-webservice/real-social-set1.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-social-set2.yml";
            + "C:\\config\\real-application.yml,"
            + "C:\\config\\local-social.yml,"
            + "C:\\config\\real-social-set1.yml,"
            + "C:\\config\\real-social-set2.yml";*/

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootWebserviceApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
