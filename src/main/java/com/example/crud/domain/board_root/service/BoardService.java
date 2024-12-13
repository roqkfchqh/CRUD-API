package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.application.mapper.BoardMapper;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.application.exception.CustomException;
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
    public BoardResponseDto createPost(BoardCombinedRequestDto boardCombinedRequestDto) {
        String encodedPassword = passwordEncoder.encode(boardCombinedRequestDto.getBoardPasswordRequestDto().getPassword());
        Board board = BoardMapper.fromRequestDto(boardCombinedRequestDto.getBoardRequestDto());
        board.setPassword(encodedPassword);
        boardRepository.save(board);
        return BoardMapper.toResponseDto(board);
    }

    //read
    @Cacheable(value = "posts", key = "#id", unless="#result == null")
    public BoardResponseDto readPost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);

        updateViewCountAsync(board);
        return BoardMapper.toResponseDto(board);
    }

    //update
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    public BoardResponseDto updatePost(Long id, BoardCombinedRequestDto boardCombinedRequestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!passwordEncoder.matches(boardCombinedRequestDto.getBoardPasswordRequestDto().getPassword(), board.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        board.updatePost(
                boardCombinedRequestDto.getBoardRequestDto().getContent(),
                boardCombinedRequestDto.getBoardRequestDto().getTitle(),
                boardCombinedRequestDto.getBoardRequestDto().getCategory()
        );

        boardRepository.save(board);
        return BoardMapper.toResponseDto(board);
    }

    //like
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    public BoardResponseDto likePost(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        board.updateLiked(board.getLiked() + 1);
        boardRepository.save(board);
        return BoardMapper.toResponseDto(board);
    }

    //delete
    @CachePut(value = "posts", key = "#id", unless = "#result == null")
    @CacheEvict(value = "boards", allEntries = true)
    public void deletePost(Long id, BoardPasswordRequestDto boardPasswordRequestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        if (!passwordEncoder.matches(boardPasswordRequestDto.getPassword(), board.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        boardRepository.deleteById(id);
    }

    //paging
    @Cacheable(value = "boards", key = "#page + '-' + #size", unless="#result == null")
    public Page<BoardResponseDto> pagingBoard(int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page -1, size, Sort.by("createdDate").descending());
        return boardRepository.findAll(pageable)
                .map(BoardMapper::toResponseDto);
    }

    //category paging
    @Cacheable(value = "boards", key = "#category + '-' + #page + '-' + #size", unless="#result == null")
    public Page<BoardResponseDto> pagingCategory(String category, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        return boardRepository.findByCategory(category.toUpperCase(), pageable)
                .map(BoardMapper::toResponseDto);
    }

    //search paging
    @Cacheable(value = "boards", key = "#keyword + '-' + #page + '-' + #size", unless="#result == null")
    public Page<BoardResponseDto> pagingSearch(String keyword, int page, int size){
        extracted(page, size);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdDate").descending());
        return boardRepository.findByTitleContaining(keyword, pageable)
                .map(BoardMapper::toResponseDto);
    }

    private static void extracted(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    @Async
    public void updateViewCountAsync(Board board) {
        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);
        cacheWithDynamicTTL(board); // 비동기 처리
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
