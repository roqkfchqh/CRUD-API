package com.example.crud.controller.board.service;

import com.example.crud.controller.common.exception.ErrorCode;
import com.example.crud.controller.board.dto.BoardMapper;
import com.example.crud.controller.board.dto.BoardRequestDto;
import com.example.crud.controller.board.dto.BoardResponseDto;
import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //create
    @CacheEvict(value = "boards", allEntries = true)
    public BoardResponseDto createPost(BoardRequestDto boardRequestDto) {
        String encodedPassword = passwordEncoder.encode(boardRequestDto.getPassword());
        BoardDb boardDb = BoardMapper.fromRequestDto(boardRequestDto);
        boardDb.setPassword(encodedPassword);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //read
    @Cacheable(value = "posts", key = "#id")
    public BoardResponseDto readPost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boardDb.updateCount(boardDb.getCount() + 1);
        boardRepository.save(boardDb);

        updateViewCountAsync(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //update
    @CachePut(value = "posts", key = "#id")
    public BoardResponseDto updatePost(Long id, BoardRequestDto boardRequestDto) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!passwordEncoder.matches(boardRequestDto.getPassword(), boardDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        boardDb.updatePost(boardRequestDto.getContent(), boardRequestDto.getTitle(), boardRequestDto.getCategory());

        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //like
    @CachePut(value = "posts", key = "#id")
    public BoardResponseDto likePost(Long id) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boardDb.updateLiked(boardDb.getLiked() + 1);
        boardRepository.save(boardDb);
        return BoardMapper.toResponseDto(boardDb);
    }

    //delete
    @CachePut(value = "posts", key = "#id")
    @CacheEvict(value = "boards", allEntries = true)
    public void deletePost(Long id, String password) {
        BoardDb boardDb = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!passwordEncoder.matches(password, boardDb.getPassword())) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        boardRepository.deleteById(id);
    }

    //paging
    @Cacheable(value = "boards", key = "#page + '-' + #size")
    public Page<BoardResponseDto> pagingBoard(int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page -1, size, Sort.by("createDate").descending());
        return boardRepository.findAll(pageable)
                .map(BoardMapper::toResponseDto);
    }

    //category paging
    @Cacheable(value = "boards", key = "#category + '-' + #page + '-' + #size")
    public Page<BoardResponseDto> pagingCategory(String category, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending());
        return boardRepository.findByCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toResponseDto);
    }

    //search paging
    @Cacheable(value = "boards", key = "#keyword + '-' + #page + '-' + #size")
    public Page<BoardResponseDto> pagingSearch(String keyword, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createDate").descending());
        return boardRepository.findByTitleContaining(keyword, pageable)
                .map(BoardMapper::toResponseDto);
    }

    private static void extracted(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    @Async
    public void updateViewCountAsync(BoardDb boardDb) {
        boardDb.updateCount(boardDb.getCount() + 1);
        boardRepository.save(boardDb);
        cacheWithDynamicTTL(boardDb); // 비동기 처리
    }

    public void cacheWithDynamicTTL(BoardDb boardDb) {
        String cacheKey = "post: " + boardDb.getId();

        long viewThreshold = 100;   //조회수 임계값
        long likeThreshold = 30;    //좋아요 임계값
        Duration ttl = Duration.ofSeconds(20);  //기본 ttl

        if(boardDb.getCount() > viewThreshold || boardDb.getLiked() > likeThreshold){
            ttl = Duration.ofSeconds(180);  //hot data 는 ttl 3분으로 연장
        }
        redisTemplate.opsForValue().set(cacheKey, boardDb, ttl);    //캐시에 저장, ttl 설정
    }

}
