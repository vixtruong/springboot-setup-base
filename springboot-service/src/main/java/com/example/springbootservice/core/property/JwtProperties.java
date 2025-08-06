package com.example.springbootservice.core.property;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private String issuer;
    private String audience;

    public static String STATIC_SECRET_KEY;
    public static String STATIC_ISSUER;
    public static String STATIC_AUDIENCE;

    @PostConstruct
    public void initStatic() {
        STATIC_SECRET_KEY = secretKey;
        STATIC_ISSUER = issuer;
        STATIC_AUDIENCE = audience;
    }
}

