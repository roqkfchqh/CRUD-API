package com.example.crud.controller.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    private final ObjectMapper objectMapper;

    public CacheConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // key -> string, value -> object
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // Redis와 연결 관리
        template.setConnectionFactory(redisConnectionFactory);
        // key를 string으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // value를 json으로 직렬화 / 역직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

        // bean으로 등록
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("localhost");
        config.setPort(6379);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) { // Redis 서버와의 연결을 생성하는 데 사용되는 팩토리 클래스
        // 설정 구성
        // ObjectMapper : Java 객체를 JSON으로 직렬화하거나 JSON을 Java 객체로 역직렬화하는 데 사용
        ObjectMapper objectMapper = new ObjectMapper();
        // activateDefaultTyping : 객체가 직렬화될 때 해당 타입 정보를 JSON에 포함시킬 수 있고, 이를 이용해 역직렬화 할 때 어떤 클래스의 인스턴스인지 확읺한다.
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // Redis를 이용해서 Spring Cache를 사용할 때 Redis 관련 설정을 모아두는 클래스
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                // null을 캐싱 할것인지
                .disableCachingNullValues()
                // 기본 캐시 유지 시간 (Time To Live)
                .entryTtl(Duration.ofSeconds(20))
                // 캐시를 구분하는 접두사 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할것인지
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))); // Value 직렬화

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }

}