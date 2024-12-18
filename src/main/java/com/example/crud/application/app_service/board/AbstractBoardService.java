package com.example.crud.application.app_service.board;

import com.example.crud.application.dto.board.BoardRequestDto;
import com.example.crud.application.dto.board.BoardResponseDto;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractBoardService {

    public final BoardResponseDto createPost(BoardRequestDto dto, Object userInfo){
        validateUser(userInfo);
        return executeCreatePost(dto, userInfo);
    }

    public final BoardResponseDto updatePost(BoardRequestDto dto, Object userInfo, Long id){
        validateUserForUpdate(userInfo, id);
        return executeUpdatePost(dto, id);
    }

    public final void deletePost(Object userInfo, Long id){
        validateUserForDelete(userInfo, id);
        executeDeletePost(id);
    }

    protected abstract void validateUser(Object userInfo);

    protected abstract void validateUserForUpdate(Object userInfo, Long id);

    protected abstract void validateUserForDelete(Object userInfo, Long id);

    protected abstract BoardResponseDto executeCreatePost(BoardRequestDto dto, Object userInfo);

    protected abstract BoardResponseDto executeUpdatePost(BoardRequestDto dto, Long id);

    protected abstract void executeDeletePost(Long id);
}
