package com.example.crud.infrastructure.limit;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RedisRateLimiter {

    private static final int USER_CREATE = 10;
    private static final int ANONYMOUS_CREATE = 2;
    private static final int USER_LIKE = 2;
    private static final int ANONYMOUS_LIKE = 1;

    private final RedisTemplate<String, Object> redisTemplate;

    public void isAllowed(String key) {
        int limit = key.startsWith("rate_limit:user:") ? USER_CREATE : ANONYMOUS_CREATE;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        }

        if(count > limit){
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }

    public void isAllowedLike(String key, Long boardId){
        String redisKey = key + ":post:like" + boardId;

        int limit = key.startsWith("rate_limit:user:") ? USER_LIKE : ANONYMOUS_LIKE;
        Long count = redisTemplate.opsForValue().increment(redisKey);

        if(count == 1){
            redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        }

        if(count > limit){
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }
}
