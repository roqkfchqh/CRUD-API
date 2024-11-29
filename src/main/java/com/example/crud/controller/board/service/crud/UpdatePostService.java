package com.example.crud.controller.board.service.crud;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;
import com.example.crud.controller.board.repository.BoardRepository;
import com.example.crud.controller.common.exception.BadInputException;

public class UpdatePostService extends PasswordRequiredService {
    private final BoardRepository boardRepository;
    private final Long boardId;
    private final String title;
    private final String content;
    private final Category category;

    public UpdatePostService(BoardRepository boardRepository, Long boardId, String title, String content, Category category) {
        this.boardRepository = boardRepository;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    @Override
    protected void performAction() {
        BoardDb board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadInputException("존재하지 않는 게시물입니다."));
        board.setTitle(title);
        board.setContent(content);
        board.setCategory(category);
        boardRepository.save(board);
        System.out.println("게시글이 수정되었습니다.");
    }


}
