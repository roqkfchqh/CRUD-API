package com.example.crud.controller.board.service.crud;

import com.example.crud.controller.board.entity.BoardDb;
import com.example.crud.controller.board.entity.Category;
import com.example.crud.controller.board.repository.BoardRepository;

public class CreatePostService {
    private final BoardRepository boardRepository;
    private final String title;
    private final String content;
    private final String nickname;
    private final String password;
    private final Category category;
    private final BoardDb board = new BoardDb();

    public CreatePostService(BoardRepository boardRepository, String title, String content, String nickname, String password, Category category) {
        this.boardRepository = boardRepository;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.category = category;
    }

    protected void createPost(){
        board.setTitle(title);
        board.setContent(content);
        board.setNickname(nickname);
        board.setPassword(password);
        board.setCategory(category);
        boardRepository.save(board);
        System.out.println("게시글이 업로드되었습니다.");
    }
}
