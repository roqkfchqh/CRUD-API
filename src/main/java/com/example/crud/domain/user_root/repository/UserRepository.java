package com.example.crud.domain.user_root.repository;

import com.example.crud.domain.user_root.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Long findIdByEmail(String email);

    @Query("SELECT u.name FROM User u WHERE u.id = :id")
    String findNameById(@Param("id") Long id);

    @Query("SELECT u.password FROM User u WHERE u.id = :id")
    String findPasswordById(Long id);

    boolean existsByEmail(String email);
}
