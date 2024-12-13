package com.example.crud.domain.repository;

import com.example.crud.infrastructure.persistence.BoardRepositoryCustom;
import com.example.crud.domain.model.entities.BoardDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardDb, Long>, BoardRepositoryCustom {

    Page<BoardDb> findAll(Pageable pageable);

    Page<BoardDb> findByCategory(String category, Pageable pageable);

    Page<BoardDb> findByTitleContaining(String title, Pageable pageable);
}
