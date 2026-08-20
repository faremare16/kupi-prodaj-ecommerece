package com.faruk.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/api/**")
                        .allowedOrigins(
                                "http://localhost:4200",
                                "https://kupi-prodaj-ecommerece-1.onrender.com"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Mapira /uploads/**
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/");

                // MAPIRA /product_pictures/** DIREKTNO NA FOLDER NA DISKU
                registry.addResourceHandler("/product_pictures/**")
                        .addResourceLocations("file:product_pictures/");
            }
        };
    }
}
