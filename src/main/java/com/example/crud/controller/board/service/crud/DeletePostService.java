package com.example.crud.controller.board.service.crud;

import com.example.crud.controller.board.repository.BoardRepository;

public class DeletePostService extends PasswordRequiredService {
    private final BoardRepository boardRepository;
    private final Long boardId;

    public DeletePostService(BoardRepository boardRepository, Long boardId) {
        this.boardRepository = boardRepository;
        this.boardId = boardId;
    }

    @Override
    protected void performAction() {
        boardRepository.deleteById(boardId);
        System.out.println("게시글이 삭제되었습니다.");
    }
}
