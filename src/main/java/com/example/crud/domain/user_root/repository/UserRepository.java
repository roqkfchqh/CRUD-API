package com.example.crud.domain.user_root.repository;

import com.example.crud.domain.user_root.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    String findIdByEmail(String email);
}
