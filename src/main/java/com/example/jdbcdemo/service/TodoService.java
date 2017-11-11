package com.example.jdbcdemo.service;

import com.example.jdbcdemo.model.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getUserTodos();

    Todo newTodo(Todo todo);

    Todo updateTodo(Todo todo);

    void deleteTodo(Long id);
}
