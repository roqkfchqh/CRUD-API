package com.example.crud.infrastructure.cache;

import com.example.crud.domain.board_root.aggregate.Board;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(customCacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, CustomCacheable customCacheable) throws Throwable {
        String cacheKey = getKey(customCacheable.key(), joinPoint);
        long ttl = customCacheable.ttl();

        //redis 에서 캐시 확인
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if(cacheValue != null){
            return cacheValue;
        }

        //캐시 없으면 메서드 실행
        Object result = joinPoint.proceed();

        //핫데이터 동적 ttl 적용
        if (result instanceof Board board) {
            if (board.getCount() > 100 || board.getLiked() > 30) {
                ttl = customCacheable.hotTtl();
            }
        }

        redisTemplate.opsForValue().set(cacheKey, result, Duration.ofSeconds(ttl));
        return result;
    }

    @Around("@annotation(customCacheEvict)")
    public Object handleCacheEvict(ProceedingJoinPoint joinPoint, CustomCacheEvict customCacheEvict) throws Throwable {
        String cacheKey = getKey(customCacheEvict.key(), joinPoint);

        Object result = joinPoint.proceed();
        redisTemplate.delete(cacheKey);
        return result;
    }

    private String getKey(String customCache, ProceedingJoinPoint joinPoint) {
        EvaluationContext context = new StandardEvaluationContext();

        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        //SpEL을 통해 키 생성
        return parser.parseExpression(customCache).getValue(context, String.class);
    }
}
