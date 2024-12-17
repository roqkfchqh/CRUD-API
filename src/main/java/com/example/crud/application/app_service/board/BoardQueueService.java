package com.example.crud.application.app_service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BoardQueueService {

    private final RedisTemplate<String, Long> redisTemplate;

    public void addToQueue(Long boardId){
        redisTemplate.opsForList().rightPush("board:viewCountQueue", boardId);
    }

    public Long fetchFromQueue(){
        return redisTemplate.opsForList().leftPop("board:viewCountQueue", 1, TimeUnit.SECONDS);
    }
}
