package com.example.crud.controller.comment.entity;

import com.example.crud.controller.board.entity.BoardDb;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comment_db")
public class CommentDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false)
    @JsonBackReference
    private BoardDb board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bigCommentId")
    @JsonBackReference
    private CommentDb bigComment;

    @OneToMany(mappedBy = "bigComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CommentDb> smallComment;

    @NotBlank
    @Size(min = 2, max = 10)
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @CreatedDate
    private LocalDateTime createDate;
}
