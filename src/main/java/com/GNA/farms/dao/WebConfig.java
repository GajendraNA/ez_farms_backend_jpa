package com.GNA.farms.dao;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = new File("src/main/resources/static").getAbsolutePath();
        registry.addResourceHandler("/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
