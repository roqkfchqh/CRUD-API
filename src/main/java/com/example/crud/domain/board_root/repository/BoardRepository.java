package com.example.crud.domain.board_root.repository;

import com.example.crud.infrastructure.persistence.BoardRepositoryCustom;
import com.example.crud.domain.board_root.aggregate.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    /**
     * TODO 전체적 수정 필요
     *
     */
    Page<Board> findAll(Pageable pageable);

    Page<Board> findByCategory(String category, Pageable pageable);

    Page<Board> findByTitleContaining(String title, Pageable pageable);

    String findPasswordById(Long id);
}
