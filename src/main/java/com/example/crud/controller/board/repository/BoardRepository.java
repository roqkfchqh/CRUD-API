package com.example.crud.controller.board.repository;

import com.example.crud.controller.board.entity.BoardDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardDb, Long>, BoardRepositoryCustom {

    Page<BoardDb> findAllBoard(Pageable pageable);

    Page<BoardDb> findAllCategory(String category, Pageable pageable);

    Page<BoardDb> findAllSearch(String title, Pageable pageable);
}
