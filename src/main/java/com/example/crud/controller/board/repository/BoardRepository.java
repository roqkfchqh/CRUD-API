package com.example.crud.controller.board.repository;

import com.example.crud.controller.board.entity.BoardDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardDb, Long>, BoardRepositoryCustom {

    @Query("SELECT b FROM BoardDb b WHERE b.title LIKE %:keyword%")
    List<BoardDb> findByTitleContaining(@Param("keyword")String keyword);

    @Query("SELECT b FROM BoardDb b WHERE b.category = :category")
    List<BoardDb> findByCategory(@Param("category") String category);

}
