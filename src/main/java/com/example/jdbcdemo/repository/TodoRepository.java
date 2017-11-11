package com.example.jdbcdemo.repository;

import com.example.jdbcdemo.model.Todo;
import com.example.jdbcdemo.model.User;

import java.util.List;

public interface TodoRepository<ID> extends BaseRepository<Todo, ID> {

    List<Todo> findAllByUser(User user);

}
