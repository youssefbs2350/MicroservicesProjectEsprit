package com.example.orderservice.kafka;

import com.example.orderservice.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "order-created";

    @Autowired
    private KafkaTemplate<String, OrderResponseDTO> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderResponseDTO order) {
        kafkaTemplate.send(TOPIC, order);
    }
}
