package com.example.crud.domain.board_root.service;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class BoardAsyncService {

    private static final int HOT_VIEW = 100;
    private static final int HOT_LIKE = 30;
    private static final int TTL = 300;
    private static final int HOT_TTL = 1200;

    private RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    @Async
    public void updateViewCountAsync(Board board) {
        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);
        cacheWithDynamicTTL(board); //비동기 처리
    }

    public void cacheWithDynamicTTL(Board board) {
        String cacheKey = "post::" + board.getId();

        int viewThreshold = HOT_VIEW;   //조회수 임계값
        int likeThreshold = HOT_LIKE;    //좋아요 임계값
        Duration ttl = Duration.ofSeconds(TTL);  //기본 ttl

        if(board.getCount() > viewThreshold || board.getLiked() > likeThreshold){
            ttl = Duration.ofSeconds(HOT_TTL);  //hot data 는 연장
        }
        redisTemplate.opsForValue().set(cacheKey, board, ttl);    //캐시에 저장, ttl 설정
    }
}
