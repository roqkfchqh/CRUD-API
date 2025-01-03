package com.example.crud.domain.board_root.entities;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.common.DateTimeEntity;
import com.example.crud.domain.user_root.aggregate.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@Getter
@Setter
@Builder
@Table(name = "comments")
public class Comment extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false, length = 10)
    private String nickname;

    @Column(updatable = false, length = 10)
    private String password;

    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parents_comment_id")
    private Comment parentsComment;

    public static Comment create(User user, String nickname, String content, Board board) {
        Comment comment = new Comment();
        comment.user = user;
        comment.nickname = nickname;
        comment.content = content;
        comment.board = board;
        return comment;
    }

    public static Comment create(String nickname, String content, Board board, String password) {
        Comment comment = new Comment();
        comment.nickname = nickname;
        comment.content = content;
        comment.board = board;
        comment.password = password;
        return comment;
    }
}
