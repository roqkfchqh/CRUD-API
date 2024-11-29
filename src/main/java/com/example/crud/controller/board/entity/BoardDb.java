package com.example.crud.controller.board.entity;

import com.example.crud.controller.comment.entity.CommentDb;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "board_db")
public class BoardDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Size(min = 2, max = 10)
    private String nickname;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category = Category.FREE;

    @Setter
    @Builder.Default
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int like = 0;

    @Setter
    @Builder.Default
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int count = 0;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CommentDb> comments = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;
}
