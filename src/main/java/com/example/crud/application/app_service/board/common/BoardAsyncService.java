package com.example.crud.application.app_service.board.common;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardAsyncService {

    private final BoardQueueService boardQueueService;

    @Async
    public void updateViewCountAsync(Long id) {
        boardQueueService.addToQueue(id);
    }
}
