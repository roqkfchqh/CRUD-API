package com.example.crud.controller.comment.repository;

import com.example.crud.controller.comment.entity.CommentDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentDb, Long>, CommentRepositoryCustom {
}
