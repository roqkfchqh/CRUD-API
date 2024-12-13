package com.example.crud.infrastructure.aspects;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.ErrorCode;
import com.example.crud.infrastructure.persistence.RedisRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class RateLimitingAspect {

    private final RedisRateLimiter redisRateLimiter;
    private final HttpServletRequest request;

    @Around("execution(* com.example.crud.interfaces.rest.BoardController.createPost(..)) || " +
            "execution(* com.example.crud.interfaces.rest.CommentController.createComment(..))")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        String clientIp = getClientIp();

        if(!redisRateLimiter.isAllowed(clientIp)){
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        return joinPoint.proceed();
    }

    private String getClientIp() {
        String ip = request.getHeader("x-forwarded-for");
        return(ip != null) ? ip.split(",")[0] : request.getRemoteAddr();
    }
}
