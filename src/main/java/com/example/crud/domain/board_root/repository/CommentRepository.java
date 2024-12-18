package com.example.crud.domain.board_root.repository;

import com.example.crud.domain.board_root.entities.Comment;
import com.example.crud.infrastructure.persistence.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Query("SELECT c.password FROM Comment c WHERE c.id = :id")
    String findPasswordById(Long id);
}
