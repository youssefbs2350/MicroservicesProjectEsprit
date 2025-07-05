package com.example.orderservice.service;

import com.example.orderservice.dto.OrderResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerTestService {

    @KafkaListener(topics = "order-created", groupId = "test-group")
    public void consume(OrderResponseDTO order) {
        System.out.println("✅ Kafka Message Received:");
        System.out.println("Product ID: " + order.getProductId());
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Quantity: " + order.getQuantity());
    }
}
