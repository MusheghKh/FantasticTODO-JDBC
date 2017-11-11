package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.User;

public interface UserRepository<ID> extends BaseRepository<User, ID> {

    User findByUsername(String username);
}
