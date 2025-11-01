package com.faycal.todoconsumer.interfaces;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.faycal.todoconsumer.application.service.TodoService;
import com.faycal.todoconsumer.domain.dto.TodoDTO;

@Component
public class TodoListener {
    
    private final TodoService todoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TodoListener(TodoService todoService) {
        this.todoService = todoService;
    }

    @KafkaListener(topics = "todo-topic", groupId = "todo-group")
    public void listen(String message) {
        System.out.println("Received Todo: " + message);
        TodoDTO todo;
        try {
            todo = objectMapper.readValue(message, TodoDTO.class);
            todoService.saveTodo(todo);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
     
    }
}
