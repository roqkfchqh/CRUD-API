package com.example.crud.controller.board.service.crud;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.common.exception.BadInputException;

public class ReadPostService {
    private final BoardRepository boardRepository;
    private Long boardId;

    public ReadPostService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void likeAction(){
        BoardDb boardDb = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadInputException("존재하지 않는 게시물입니다."));
        boardDb.setLiked(boardDb.getLiked() + 1);
        System.out.println("좋아용~");
    }

    public void clickAction(){
        BoardDb boardDb = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadInputException("존재하지 않는 게시물입니다."));
        boardDb.setCount(boardDb.getCount() + 1);
        System.out.println("조회수!!!");
    }
}
