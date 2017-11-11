package com.example.jdbcdemo.service;

import com.example.jdbcdemo.model.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);
}
