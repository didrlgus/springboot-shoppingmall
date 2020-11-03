package com.shoppingmall;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MailApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            //+ "/home/ec2-user/app/config/springboot-webservice/real-mail-application.yml";
            + "C:\\config\\real-mail-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(MailApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
