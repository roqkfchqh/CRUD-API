package com.example.crud.domain.board_root.repository;

import com.example.crud.infrastructure.persistence.BoardRepositoryCustom;
import com.example.crud.domain.board_root.aggregate.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Page<Board> findByCategory(String category, Pageable pageable);

    Page<Board> findByTitleContaining(String title, Pageable pageable);

    void deleteByUserId(Long userId);

    String findPasswordById(Long id);


}
