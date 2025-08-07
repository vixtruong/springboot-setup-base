package com.example.springbootservice.service;

import com.example.springbootservice.core.constant.RedisKeyConstants;
import com.example.springbootservice.ultil.JWTUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtils jwtUtils;

    public RedisService(RedisTemplate<String, Object> redisTemplate, JWTUtils jwtUtils) {
        this.redisTemplate = redisTemplate;
        this.jwtUtils = jwtUtils;
    }

    public void set(String key, Object value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(timeoutInSeconds));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setBacklist(String jwt) {
        String tokenId = jwtUtils.extractTokenId(jwt);
        Date expiration = jwtUtils.extractExpiration(jwt);

        long timeoutInSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;

        if (timeoutInSeconds > 0) {
            String key = RedisKeyConstants.blacklistKey(tokenId);
            set(key, jwt, timeoutInSeconds);
        }
    }

    public boolean isInBlacklist(String jwt) {
        String tokenId = jwtUtils.extractTokenId(jwt);
        String key = RedisKeyConstants.blacklistKey(tokenId);
        return redisTemplate.hasKey(key);
    }
}
