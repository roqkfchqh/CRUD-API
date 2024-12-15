package com.example.crud.infrastructure.limit;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.user_root.aggregate.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    //create limit
    @Around("execution(* com.example.crud.interfaces.rest.board.BoardController.createPost(..)) || " +
            "execution(* com.example.crud.interfaces.rest.comment.CommentController.createComment(..))")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = getKey();
        redisRateLimiter.isAllowed(key);

        return joinPoint.proceed();
    }

    //like, read limit
    @Around("execution(* com.example.crud.interfaces.rest.board.BoardController.likePost(..)) || " +
            "execution(* com.example.crud.interfaces.rest.board.BoardController.readPost(..))")
    public Object checkRateLimitLikeAndReadPost(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = getKey();
        Long boardId = getBoardId(joinPoint.getArgs());
        redisRateLimiter.isAllowedLikeAndRead(key, boardId);

        return joinPoint.proceed();
    }

    private String getClientIp() {
        String ip = request.getHeader("x-forwarded-for");
        return(ip != null) ? ip.split(",")[0] : request.getRemoteAddr();
    }

    private String getKey() {
        String clientIp = getClientIp();
        HttpSession session = request.getSession(false);
        String sessionId = (String) session.getAttribute("sessionId");
        String key;

        if(sessionId != null){
            User user = (User) session.getAttribute("user");
            key = "rate_limit:user:" + user.getId();
        }else{
            key = "rate_limit:ip:" + clientIp;
        }
        return key;
    }

    private Long getBoardId(Object[] args) {
        for(Object arg : args){
            if(arg instanceof Long){
                return (Long) arg;
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND);
    }
}
