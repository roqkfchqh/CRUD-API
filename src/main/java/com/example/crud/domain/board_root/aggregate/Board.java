package com.example.crud.domain.board_root.aggregate;

import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.domain.board_root.valueobjects.Category;
import com.example.crud.domain.common.DateTimeEntity;
import com.example.crud.domain.user_root.aggregate.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "boards")
public class Board extends DateTimeEntity {

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

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 300)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private Category category = Category.FREE;

    @Builder.Default
    @Column(nullable = false)
    private int liked = 0;

    @Builder.Default
    @Column(nullable = false)
    private int count = 0;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void updateLiked() {
        this.liked ++;
    }

    public void updateCount() {
        this.count ++;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBoard(null);
    }

    public static Board create(String title, String content, Category category, String nickname) {
        return Board.builder()
                .title(title)
                .content(content)
                .category(category)
                .nickname(nickname)
                .build();
    }

    public static Board createAnonymous(String title, String content, Category category, String nickname, String password) {
        return Board.builder()
                .title(title)
                .content(content)
                .category(category)
                .nickname(nickname)
                .password(password)
                .build();
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

}
