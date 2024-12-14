package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.domain.user_root.aggregate.User;
import com.example.crud.domain.user_root.service.UserValidationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserValidationService userValidationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private BoardValidationService boardValidationService;

    //create
    @CacheEvict(value = "boards", allEntries = true)
    public BoardResponseDto createPost(HttpServletRequest req, BoardRequestDto dto) {
        User user = userValidationService.validateUser(req);
        Board board = BoardMapper.toEntity(dto, user);

        boardRepository.save(board);

        return BoardMapper.toDto(board);
    }

    //read
    @Value("${cache.view.threshold:100}")
    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#id", unless="#result == null")
    public BoardResponseDto readPost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);

        updateViewCountAsync(board);
        return BoardMapper.toDto(board);
    }

    //update
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    public BoardResponseDto updatePost(HttpServletRequest req, Long id, BoardRequestDto dto) {
        userValidationService.validateUser(req);
        Board board = boardValidationService.validateBoard(id);

        board.updatePost(
                dto.getContent(),
                dto.getTitle(),
                dto.getCategory()
        );

        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //like
    @Value("${cache.view.threshold:30}")
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    public BoardResponseDto likePost(Long id) {
        Board board = boardValidationService.validateBoard(id);

        board.updateLiked(board.getLiked() + 1);
        boardRepository.save(board);
        return BoardMapper.toDto(board);
    }

    //delete
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    @CacheEvict(value = "boards", allEntries = true)
    public void deletePost(HttpServletRequest req, Long id){
        boardValidationService.validateBoard(id);
        userValidationService.validateUser(req);
        boardRepository.deleteById(id);
    }

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
