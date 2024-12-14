package com.example.crud.domain.board_root.service;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BoardAsyncService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    @Async
    public void updateViewCountAsync(Board board) {
        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);
        cacheWithDynamicTTL(board); //비동기 처리
    }

    public void cacheWithDynamicTTL(Board board) {
        String cacheKey = "posts: " + board.getId();

        long viewThreshold = 100;   //조회수 임계값
        long likeThreshold = 30;    //좋아요 임계값
        Duration ttl = Duration.ofSeconds(20);  //기본 ttl

        if(board.getCount() > viewThreshold || board.getLiked() > likeThreshold){
            ttl = Duration.ofSeconds(180);  //hot data 는 ttl 3분으로 연장
        }
        redisTemplate.opsForValue().set(cacheKey, board, ttl);    //캐시에 저장, ttl 설정
    }
}
