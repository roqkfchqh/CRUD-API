package com.example.crud.domain.board_root.service;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import com.example.crud.infrastructure.cache.CustomCacheEvict;
import com.example.crud.infrastructure.cache.CustomCacheable;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractBoardService {

    private static final int POST_TTL = 600;

    public final BoardResponseDto createPost(BoardRequestDto dto, Object userInfo){
        validateUser(userInfo);
        return executeCreatePost(dto, userInfo);
    }

    @CustomCacheable(key = "'post::' + #id", ttl = POST_TTL)
    public final BoardResponseDto updatePost(BoardRequestDto dto, Object userInfo, Long id){
        validateUser(userInfo);
        return executeUpdatePost(dto, id);
    }

    @CustomCacheEvict(key = "'post::' + #id")
    public final void deletePost(Object userInfo, Long id){
        validateUserForDelete(userInfo, id);
        executeDeletePost(id);
    }

    protected abstract void validateUser(Object userInfo);

    protected abstract void validateUserForDelete(Object userInfo, Long id);

    protected abstract BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo);

    protected abstract BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id);

    protected abstract void executeDeletePost(Long id);
}
