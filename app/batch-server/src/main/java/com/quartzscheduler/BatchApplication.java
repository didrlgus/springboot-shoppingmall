package com.quartzscheduler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing // JPA Auditing 활성화
@EnableScheduling   // 스케줄러 사용
@SpringBootApplication
public class BatchApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application-real.properties,"
            + "/home/ec2-user/app/config/springboot-webservice/real-quartz-application.yml";
            //+ "C:\\config\\real-quartz-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(BatchApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

