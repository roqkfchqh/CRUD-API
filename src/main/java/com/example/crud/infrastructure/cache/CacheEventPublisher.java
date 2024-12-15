package com.example.crud.infrastructure.cache;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class CacheEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(CacheEventType type, Map<String, Object> data){
        eventPublisher.publishEvent(new CacheEvent(type, data));
    }
}
