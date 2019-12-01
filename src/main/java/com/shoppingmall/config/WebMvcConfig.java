package com.shoppingmall.config;

import com.shoppingmall.interceptor.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 생성자 주입
    private HandlerInterceptor handlerInterceptor;
    private Environment environment;

    public WebMvcConfig(HandlerInterceptor handlerInterceptor, Environment environment) {
        this.handlerInterceptor = handlerInterceptor;
        this.environment = environment;
    }

    private static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/static/";
    @Value("${file.upload-dir}")
    private String uploadsRoot;
    @Value("{file.product-upload-dir}")
    private String productUploadsRoot;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor)
                .addPathPatterns("/*");

        WebMvcConfigurer.super.addInterceptors(registry);
    }

    // 정적 리소스 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"upload/").setCachePeriod(31536000);
        registry.addResourceHandler("/ajax-load/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"ajax-load/").setCachePeriod(31536000);
        registry.addResourceHandler("/color-switcher/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"color-switcher/").setCachePeriod(31536000);
        registry.addResourceHandler("/css/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"css/").setCachePeriod(31536000);
        registry.addResourceHandler("/fonts/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"fonts/").setCachePeriod(31536000);
        registry.addResourceHandler("/images/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"images/").setCachePeriod(31536000);
        registry.addResourceHandler("/includes/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"includes/").setCachePeriod(31536000);
        registry.addResourceHandler("/js/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"js/").setCachePeriod(31536000);

        String profile = environment.getActiveProfiles()[0];

        // 외부 resource에 접근 가능 (/upload url 요청 시 로컬 디스크의 /upload 폴더로 접근)
        // 로컬 (개발 환경)
        if (profile.equals("local")) {
            registry.addResourceHandler("/review-upload-image/**").addResourceLocations("file:///review-upload-image/").setCachePeriod(31536000);
            registry.addResourceHandler("/product-upload-image/**").addResourceLocations("file:///product-upload-image/").setCachePeriod(31536000);
        } else {    // AWS EC2
            registry.addResourceHandler("/review-upload-image/**").addResourceLocations("file:///home/ec2-user/app/images/review-upload-image/").setCachePeriod(31536000);
            registry.addResourceHandler("/product-upload-image/**").addResourceLocations("file:///home/ec2-user/app/images/product-upload-image/").setCachePeriod(31536000);
        }
    }
}
