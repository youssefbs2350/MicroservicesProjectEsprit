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
        try {
            kafkaTemplate.send(TOPIC, order);
        } catch (Exception e) {
            System.err.println("❌ Kafka not available. Skipping event publish. Reason: " + e.getMessage());
            // Tu peux logguer ou ignorer selon le besoin
        }

    }
}
