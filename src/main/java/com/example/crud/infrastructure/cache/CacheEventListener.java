package com.example.crud.infrastructure.cache;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CacheEventListener {

    private final RedisTemplate<String, Object> redisTemplate;

    public CacheEventListener(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    public void handleCacheEvent(CacheEvent event) {
        switch (event.type()) {
            case POST_PUT -> handlePostCachePut(event.data());
            case POST_EVICT -> handlePostCacheEvict(event.data());
            case PAGE_ABLE -> handlePageCache(event.data());
            case CATEGORY_ABLE -> handleCategoryCache(event.data());
            case SEARCH_ABLE -> handleSearchCache(event.data());
            default -> throw new CustomException(ErrorCode.EVENT_NOT_ACCEPTABLE);
        }
    }

    @Async
    public void handlePostCachePut(Map<String, Object> data){
        String cacheKey = generateCacheKey("post", data);
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    @Async
    public void handlePostCacheEvict(Map<String, Object> data){
        String cacheKey = generateCacheKey("post", data);
        redisTemplate.delete(cacheKey);
    }

    @Async
    public void handlePageCache(Map<String, Object> data){
        String cacheKey = generateCacheKey("page", data);
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    @Async
    public void handleCategoryCache(Map<String, Object> data){
        String cacheKey = generateCacheKey("category", data);
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    @Async
    public void handleSearchCache(Map<String, Object> data){
        String cacheKey = generateCacheKey("keyword", data);
        redisTemplate.opsForValue().set(cacheKey, data);
    }

    private String generateCacheKey(String prefix, Map<String, Object> data) {
        if (data.containsKey("boardId")) {
            return prefix + data.get("boardId");
        } else if (data.containsKey("page")) {
            return prefix + data.get("page") + ":" + data.get("size");
        }else if(data.containsKey("category")){
            return prefix + data.get("category") + ":" + data.get("page") + ":" + data.get("size");
        } else if (data.containsKey("keyword")) {
            return prefix + data.get("keyword") + ":" + data.get("page") + ":" + data.get("size");
        }
        throw new CustomException(ErrorCode.BAD_GATEWAY);
    }
}
