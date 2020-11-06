package com.shoppingmall;

import com.shoppingmall.publisher.MessagePublisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(excludeFilters  = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {MessagePublisher.class})})
@SpringBootApplication
public class OrderApplication {
    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-order-application.yml";
            //+ "C:\\config\\real-order-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(OrderApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
