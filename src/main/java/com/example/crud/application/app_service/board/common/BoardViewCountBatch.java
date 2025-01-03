package com.example.crud.application.app_service.board.common;

import com.example.crud.application.exception.CustomException;
import com.example.crud.application.exception.errorcode.ErrorCode;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardViewCountBatch {

    private final BoardQueueService boardQueueService;
    private final BoardRepository boardRepository;

    @Scheduled(fixedDelay = 5000)
    public void processViewCountBatch(){
        Long boardId;

        while((boardId = boardQueueService.fetchFromQueue()) != null){
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

            if(board != null){
                board.updateCount();
                boardRepository.save(board);
            }
        }
    }
}
