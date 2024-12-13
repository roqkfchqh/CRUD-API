package com.example.crud.infrastructure.persistence;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RedisRateLimiter {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean isAllowed(String ip) {
        String key = "rate_limit: " + ip;

        Boolean exists = redisTemplate.hasKey(key);
        if(exists) {
            return false;
        }

        redisTemplate.opsForValue().set(key, "1", 30, TimeUnit.SECONDS);
        return true;
    }
}
