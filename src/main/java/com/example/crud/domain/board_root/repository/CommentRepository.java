package com.example.crud.domain.board_root.repository;

import com.example.crud.infrastructure.persistence.CommentRepositoryCustom;
import com.example.crud.domain.board_root.aggregate.Board;
import com.example.crud.domain.board_root.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findByBoard(Board board, Pageable pageable);
}
