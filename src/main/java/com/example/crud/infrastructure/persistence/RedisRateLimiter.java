package com.example.crud.infrastructure.persistence;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RedisRateLimiter {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean isAllowed(String key) {
        int limit = key.startsWith("rate_limit:user:") ? 10 : 2;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }

        return count <= limit;
    }

    public boolean isAllowedLikeAndRead(String key, Long boardId){
        String redisKey = key + ":post:" + boardId;

        int limit = key.startsWith("rate_limit:user:") ? 2 : 1;
        Long count = redisTemplate.opsForValue().increment(redisKey);

        if(count == 1){
            redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        }

        return count <= limit;
    }
}
