//package com.example.crud.infrastructure.cache;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.CacheKeyPrefix;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//@Configuration
//@EnableCaching
//@RequiredArgsConstructor
//public class CacheConfig {
//
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        // Redis 를 이용해서 Spring Cache 를 사용할 때 Redis 관련 설정을 모아두는 클래스
//        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                // null 을 캐싱 할것인지
//                .disableCachingNullValues()
//                // 캐시를 구분하는 접두사 설정
//                .computePrefixWith(CacheKeyPrefix.simple())
//                // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할것인지
//                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(new GenericJackson2JsonRedisSerializer(mapper))); // Value 직렬화
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(defaultConfig)
//                .build();
//    }
//
//}