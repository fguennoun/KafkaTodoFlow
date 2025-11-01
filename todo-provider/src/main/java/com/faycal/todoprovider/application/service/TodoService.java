package com.faycal.todoprovider.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.faycal.todoprovider.domain.dto.TodoDTO;
import com.faycal.todoprovider.infrastructure.messaging.TodoProducer;

@Service
public class TodoService {

    private final TodoProducer todoProducer;

    public TodoService(TodoProducer todoProducer) {
        this.todoProducer = todoProducer;
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        // Assign a unique ID to the Todo
        todoDTO.setId(UUID.randomUUID().toString());
        // Send to Kafka
        todoProducer.sendMessage(todoDTO);

        return todoDTO;
    }
    
}