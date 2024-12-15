package com.example.crud.domain.board_root.service;

import com.example.crud.infrastructure.cache.CacheEventPublisher;
import com.example.crud.infrastructure.cache.CacheEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheEventPublisher cacheEventPublisher;

    public void put(Long board, CacheEventType postPut) {
        Map<String, Object> eventData = Map.of("boardId", board);
        cacheEventPublisher.publishEvent(postPut, eventData);
    }

    public void evict(Long id, CacheEventType postEvict) {
        Map<String, Object> eventData = Map.of("boardId", id);
        cacheEventPublisher.publishEvent(postEvict, eventData);
    }

    public void pagePut(int page, int size) {
        Map<String, Object> eventData = Map.of("page", page, "size", size);
        cacheEventPublisher.publishEvent(CacheEventType.PAGE_ABLE, eventData);
    }

    public void categoryPut(String category, int page, int size) {
        Map<String, Object> eventData = Map.of("category", category, "page", page, "size", size);
        cacheEventPublisher.publishEvent(CacheEventType.PAGE_ABLE, eventData);
    }

    public void searchPut(String keyword, int page, int size) {
        Map<String, Object> eventData = Map.of("keyword", keyword, "page", page, "size", size);
        cacheEventPublisher.publishEvent(CacheEventType.PAGE_ABLE, eventData);
    }
}
