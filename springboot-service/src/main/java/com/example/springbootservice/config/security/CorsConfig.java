package com.example.springbootservice.config.security;

import com.example.springbootservice.core.property.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(CorsProperties.ALLOWED_ORIGINS)
                        .allowedMethods(CorsProperties.ALLOWED_METHODS)
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
