package com.example.crud.controller;

import com.example.crud.controller.Main;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<Main, Integer> {
}
