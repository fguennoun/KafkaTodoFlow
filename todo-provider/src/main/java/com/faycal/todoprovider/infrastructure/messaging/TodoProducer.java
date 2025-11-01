package com.faycal.todoprovider.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.faycal.todoprovider.domain.dto.TodoDTO;

@Component
public class TodoProducer {
    
    private final KafkaTemplate<String, TodoDTO> kafkaTemplate;

    private final static String TOPIC_NAME = "todo-topic";

    public TodoProducer(KafkaTemplate<String, TodoDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(TodoDTO todoDTO) {
        kafkaTemplate.send(TOPIC_NAME, todoDTO.getId(), todoDTO);
        System.out.println("Sent TODO to Kafka: " + todoDTO);
    }
}
