package com.example.crud.infrastructure.cache;

import java.util.Map;

public record CacheEvent(CacheEventType type, Map<String, Object> data) {

}
