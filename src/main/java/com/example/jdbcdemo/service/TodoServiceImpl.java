package com.example.jdbcdemo.service;

import com.example.jdbcdemo.model.Todo;
import com.example.jdbcdemo.model.User;
import com.example.jdbcdemo.repository.TodoRepositoryImpl;
import com.example.jdbcdemo.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final UserRepositoryImpl userRepository;
    private final TodoRepositoryImpl todoRepository;
    private final SecurityService securityService;

    @Autowired
    public TodoServiceImpl(UserRepositoryImpl userRepository, TodoRepositoryImpl todoRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.securityService = securityService;
    }

    @Override
    public List<Todo> getUserTodos() {
        User currentUser = userRepository.findByUsername(securityService.findLoggedInUsername());

        List<Todo> data = todoRepository.findAllByUser(currentUser);
        return data;
    }

    @Override
    public Todo newTodo(Todo todo) {
        User currentUser = userRepository.findByUsername(securityService.findLoggedInUsername());

        todo.setCompleted(false);
        todo.setUser_id(currentUser.getId());

        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Todo todo) {
        Todo todoData = todoRepository.findOne(todo.getId());
        if (todoData == null){
            return null;
        }
        todoData.setTitle(todo.getTitle());
        todoData.setCompleted(todo.getCompleted());
        return todoRepository.save(todoData);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.delete(id);
    }
}
