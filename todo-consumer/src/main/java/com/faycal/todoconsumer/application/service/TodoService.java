package com.faycal.todoconsumer.application.service;

import com.faycal.todoconsumer.domain.model.Todo;
import com.faycal.todoconsumer.infrastructure.persistence.TodoRepository;

import org.springframework.stereotype.Service;

import com.faycal.todoconsumer.domain.dto.TodoDTO;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void saveTodo(TodoDTO todoDTO) {
        Todo todo = new Todo(todoDTO.getId(), todoDTO.getTitle(), todoDTO.isCompleted());
        todoRepository.save(todo);
        System.out.println("Saved TODO in H2 Database: " + todo);
    }
    
}
