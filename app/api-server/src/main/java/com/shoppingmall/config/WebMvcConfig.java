package com.shoppingmall.config;

import com.shoppingmall.common.AWSS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 생성자 주입
    //private final HandlerInterceptor handlerInterceptor;
    private final AWSS3Properties awss3Properties;

    private static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/static/";
    private static final Integer ONE_YEAR = 31536000;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(handlerInterceptor)
//                .addPathPatterns("/*");
//
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }

    // 정적 리소스 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"upload/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/ajax-load/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"ajax-load/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/color-switcher/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"color-switcher/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/css/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"css/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/fonts/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"fonts/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/images/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"images/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/includes/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"includes/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler("/js/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS+"js/").setCachePeriod(ONE_YEAR);

        String s3ImgUploadPath = awss3Properties.getImgUploadPath();

        registry.addResourceHandler(s3ImgUploadPath + "/**").addResourceLocations(s3ImgUploadPath + "/").setCachePeriod(ONE_YEAR);
        registry.addResourceHandler(s3ImgUploadPath + "/**").addResourceLocations(s3ImgUploadPath + "/").setCachePeriod(ONE_YEAR);
    }
}
