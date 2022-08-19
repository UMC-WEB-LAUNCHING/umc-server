package com.umc.helper.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor2 jwtTokenInterceptor2;



    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtTokenInterceptor2)
//                .addPathPatterns("/member/edit/**")
//                .addPathPatterns("/bookmark/**")
//                .addPathPatterns("/folder/**")
//                .addPathPatterns("/search")
//                .addPathPatterns("/team/**")
//                .addPathPatterns("/trash/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*");
    }
}