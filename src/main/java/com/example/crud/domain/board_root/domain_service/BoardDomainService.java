package com.example.crud.domain.board_root.domain_service;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.user_root.aggregate.User;
import org.springframework.stereotype.Service;

@Service
public class BoardDomainService {

    public Comment createComment(User user, String nickname, String content, Board board){
        Comment comment = Comment.create(user, nickname, content, board);
        board.addComment(comment);
        return comment;
    }

    public Comment createAnonymousComment(String nickname, String content, Board board, String password){
        Comment comment = Comment.createAnonymous(nickname, content, board, password);
        board.addComment(comment);
        return comment;
    }
}
