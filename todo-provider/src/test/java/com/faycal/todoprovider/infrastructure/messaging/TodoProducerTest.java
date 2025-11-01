package com.faycal.todoprovider.infrastructure.messaging;

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

import com.faycal.todoprovider.domain.dto.TodoDTO;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "todo-topic")
public class TodoProducerTest {

    @Autowired
    private TodoProducer todoProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void testSendMessage() {

        TodoDTO todo = new TodoDTO("1", "Test Kafka", false);
        todoProducer.sendMessage(todo);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
        
        // Use JsonDeserializer with the correct type
        JsonDeserializer<TodoDTO> jsonDeserializer = new JsonDeserializer<>(TodoDTO.class);
        jsonDeserializer.addTrustedPackages("*"); // Or specify your package
        
        DefaultKafkaConsumerFactory<String, TodoDTO> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), jsonDeserializer);
        
        Consumer<String, TodoDTO> consumer = consumerFactory.createConsumer();
        
        // Subscribe the consumer to the topic
        consumer.subscribe(Collections.singletonList("todo-topic"));
        
        // Now get the record using Duration
        ConsumerRecord<String, TodoDTO> record = KafkaTestUtils.getSingleRecord(consumer, "todo-topic", Duration.ofSeconds(10));

        assertThat(record).isNotNull();
        assertThat(record.value()).isNotNull();
        assertThat(record.value().getTitle()).isEqualTo("Test Kafka");
    }
}
