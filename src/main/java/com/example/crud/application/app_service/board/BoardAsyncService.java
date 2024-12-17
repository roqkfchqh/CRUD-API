package com.example.crud.application.app_service.board;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.domain_service.BoardDomainService;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardAsyncService {
    private final BoardRepository boardRepository;
    private final BoardDomainService boardDomainService;

    @Async
    public void updateViewCountAsync(Board board) {
        boardDomainService.countBoard(board);
        boardRepository.save(board);
    }
}
