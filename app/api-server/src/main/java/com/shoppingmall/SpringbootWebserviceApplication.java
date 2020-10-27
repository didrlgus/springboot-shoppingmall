package com.shoppingmall;

import com.shoppingmall.common.AWSS3Properties;
import com.shoppingmall.common.ImpProperties;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing // JPA Auditing 활성화
@EnableBatchProcessing // 배치 작업에 필요한 빈을 미리 등록
@EnableScheduling // 스케줄러 사용
@EnableAspectJAutoProxy // AOP 사용, 스프링이 자동으로 개발자의 메서드를 호출하기 전에 가로챌 수 있게 하는 옵션.
@EnableConfigurationProperties({ImpProperties.class, AWSS3Properties.class})
@SpringBootApplication
public class SpringbootWebserviceApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-application.yml";
            //+ "C:\\config\\local-social.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringbootWebserviceApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
