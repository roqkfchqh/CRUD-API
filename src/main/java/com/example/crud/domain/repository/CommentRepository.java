package com.example.crud.domain.repository;

import com.example.crud.infrastructure.persistence.CommentRepositoryCustom;
import com.example.crud.domain.model.entities.BoardDb;
import com.example.crud.domain.model.entities.CommentDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentDb, Long>, CommentRepositoryCustom {

    Page<CommentDb> findByBoard(BoardDb board, Pageable pageable);
}
