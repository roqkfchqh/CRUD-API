package com.example.crud.domain.board_root.entities;

import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.common.DateTimeEntity;
import com.example.crud.domain.user_root.aggregate.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(updatable = false)
    private String nickname;

    private String password;

    @Column(nullable = false)
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

    public void updateContent(String content) {
        this.content = content;
    }

}
