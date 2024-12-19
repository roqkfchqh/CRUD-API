package com.example.crud.domain.board_root.entities;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.common.DateTimeEntity;
import com.example.crud.domain.user_root.aggregate.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @JsonBackReference
    private Comment parentsComment;

    @OneToMany(mappedBy = "parentsComment")
    @JsonManagedReference
    private List<Comment> childComment;

    public static Comment create(User user, String nickname, String content, Board board) {
        return new Comment(
                user,
                nickname,
                content,
                board);
    }

    public static Comment create(String nickname, String content, Board board, String password) {
        return new Comment(
                nickname,
                content,
                board,
                password);
    }

    private Comment(User user, String nickname, String content, Board board) {
        this.user = user;
        this.nickname = nickname;
        this.content = content;
        this.board = board;
    }

    private Comment(String nickname, String content, Board board, String password) {
        this.nickname = nickname;
        this.content = content;
        this.board = board;
        this.password = password;
    }
}
