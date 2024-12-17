package com.example.crud.domain.user_root.service;

import com.example.crud.domain.user_root.aggregate.User;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

    public User createUser(String name, String email, String password) {
        return User.create(name, email, password);
    }

    public void updateUser(User user, String name, String password){
        user.update(name, password);
    }
}
