package com.example.crud.application.app_service.board.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardQueueService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void addToQueue(Long boardId){
        redisTemplate.opsForList().rightPush("board:viewCountQueue", boardId);
    }

    public Long fetchFromQueue(){
        Object value = redisTemplate.opsForList().leftPop("board:viewCountQueue");
        if(value instanceof Integer){
            return ((Integer) value).longValue();
        }
        return (Long) value;
    }
}
