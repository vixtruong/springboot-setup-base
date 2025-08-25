package com.example.springbootservice.core.property;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsProperties {
    private String origins;
    private String methods;

    public static String[] ALLOWED_ORIGINS;
    public static String[] ALLOWED_METHODS;

    @PostConstruct
    public void initStatic() {
        ALLOWED_ORIGINS = Arrays.stream(origins.split(","))
                .map(String::trim)
                .toArray(String[]::new);
        ALLOWED_METHODS = Arrays.stream(methods.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
