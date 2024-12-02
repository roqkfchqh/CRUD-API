package com.example.crud.controller.comment.entity;

import com.example.crud.controller.board.entity.BoardDb;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comment_db")
public class CommentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference
    private BoardDb board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "big_comment_id")
    @JsonBackReference
    private CommentDb bigComment;

    @OneToMany(mappedBy = "bigComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CommentDb> smallComment;

    @NotBlank
    private String content;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @CreatedDate
    @NotNull
    private LocalDateTime createDate;

    public void updateContent(String content) {
        this.content = content;
    }
}
