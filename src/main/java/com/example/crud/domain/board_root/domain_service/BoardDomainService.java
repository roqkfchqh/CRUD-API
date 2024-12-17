package com.example.crud.domain.board_root.domain_service;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.valueobjects.Category;
import org.springframework.stereotype.Service;

@Service
public class BoardDomainService {

    public Board createBoard(String title, String content, Category category, String nickname){
        return Board.create(title, content, category, nickname);
    }

    public Board createAnonymousBoard(String title, String content, Category category, String nickname, String password){
        return Board.createAnonymous(title, content, category, nickname, password);
    }

    public void updateBoard(Board board, String title, String content, Category category){
        board.update(title, content, category);
    }

    public void likeBoard(Board board){
        board.updateLiked();
    }

    public void countBoard(Board board){
        board.updateCount();
    }

    public Comment createComment(String nickname, String content, Board board){
        Comment comment = Comment.create(nickname, content, board);
        board.addComment(comment);
        return comment;
    }

    public Comment createAnonymousComment(String nickname, String content, Board board, String password){
        Comment comment = Comment.createAnonymous(nickname, content, board, password);
        board.addComment(comment);
        return comment;
    }

    public void removeComment(Board board, Comment comment){
        board.removeComment(comment);
    }
}
