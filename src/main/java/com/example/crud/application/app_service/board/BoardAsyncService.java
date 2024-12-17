package com.example.crud.application.app_service.board;

import com.example.crud.domain.board_root.aggregate.Board;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardAsyncService {

    private final BoardQueueService boardQueueService;

    @Async
    public void updateViewCountAsync(Board board) {
        boardQueueService.addToQueue(board.getId());
    }
}
