package com.example.crud.domain.board_root.service;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import com.example.crud.infrastructure.cache.CustomCacheable;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardAsyncService {
    private final BoardRepository boardRepository;

    @Async
    @CustomCacheable(key = "'post::' + #board.id")
    public void updateViewCountAsync(Board board) {
        board.updateCount(board.getCount() + 1);
        boardRepository.save(board);
    }
}
