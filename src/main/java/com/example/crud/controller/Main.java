package com.example.crud.controller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Main {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    protected Main(){}

    public Main(int id) {
        this.id = id;
    }

    public int getId(int id) {
        return id;
    }
}
