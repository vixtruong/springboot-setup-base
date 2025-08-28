package com.example.springbootservice.config;

import com.cloudinary.Cloudinary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {
    private String url;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(url);
    }
}
