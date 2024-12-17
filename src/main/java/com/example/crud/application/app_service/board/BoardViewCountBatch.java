package com.example.crud.application.app_service.board;

import com.example.crud.application.app_service.validation.BoardValidationService;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.domain_service.BoardDomainService;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardViewCountBatch {

    private final BoardQueueService boardQueueService;
    private final BoardRepository boardRepository;
    private final BoardValidationService boardValidationService;
    private final BoardDomainService boardDomainService;

    @Scheduled(fixedDelay = 5000)
    public void processViewCountBatch(){
        Long boardId;

        while((boardId = boardQueueService.fetchFromQueue()) != null){
            Board board = boardValidationService.validateBoard(boardId);
            if(board != null){
                boardDomainService.countBoard(board);
                boardRepository.save(board);
            }
        }
    }
}
