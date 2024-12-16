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

@Entity
@AllArgsConstructor
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

    @Column(updatable = false)
    private String nickname;

    private String password;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    public void updatePost(String content, String title, Category category) {
        this.content = content;
        this.title = title;
        this.category = category;
    }

    public void updateLiked(int liked) {
        this.liked = liked;
    }

    public void updateCount(int count) {
        this.count = count;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBoard(null);
    }
}
