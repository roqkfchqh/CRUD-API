package com.example.crud.controller.board.repository;

import com.example.crud.controller.board.entity.BoardDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardDb, Long>, BoardRepositoryCustom {
}
