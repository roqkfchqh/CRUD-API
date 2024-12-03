package com.example.crud.controller.board.entity;

import com.example.crud.controller.comment.entity.CommentDb;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "board_db")
public class BoardDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false)
    private String password;

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
    private List<CommentDb> comments = new ArrayList<>();

    @CreatedDate
    @NotNull
    private LocalDateTime createdDate;

    public void updatePost(String content, String title, String category) {
        this.content = content;
        this.title = title;
        this.category = Category.valueOf(category);
    }

    public void updateLiked(int liked) {
        this.liked = liked;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
