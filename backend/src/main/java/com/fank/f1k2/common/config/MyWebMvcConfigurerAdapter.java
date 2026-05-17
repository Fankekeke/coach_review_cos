package com.fank.f1k2.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imagesWeb/**").addResourceLocations("file:G:/Project/20260513教练在线考核平台/db/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
