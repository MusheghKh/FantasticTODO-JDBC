package com.example.jdbcdemo.web;

import com.example.jdbcdemo.model.Todo;
import com.example.jdbcdemo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public List<Todo> getAllTodos(){
        return todoService.getUserTodos();
    }

    @PostMapping(value = "/todos", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Todo createTodo(@Valid Todo todo){
        return todoService.newTodo(todo);
    }

    @PutMapping(value = "/todos/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Todo> updateTodo(@Valid Todo todo){
        Todo updatedTodo = todoService.updateTodo(todo);
        if (updatedTodo == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/todos/{id}")
    public void deleteTodo(@PathVariable("id") Long id){
        todoService.deleteTodo(id);
    }
}
