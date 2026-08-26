package com.faruk.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // povezivanje url putanja /uplodas/** sa folderom uplods
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}
