package com.shoppingmall;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@RequiredArgsConstructor
@EnableBatchProcessing
@SpringBootApplication
public class ProductPurchaseCountBatchApplication {

    private static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/home/ec2-user/app/config/springboot-webservice/real-product-purchase-count-batch-application.yml";
            //+ "C:\\config\\real-product-purchase-count-batch-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(ProductPurchaseCountBatchApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
