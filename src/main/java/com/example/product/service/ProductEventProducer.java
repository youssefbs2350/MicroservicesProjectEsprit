package com.example.product.service;

import com.example.product.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
@Service
public class ProductEventProducer {

   // @Value("${kafka.topic.product.created}")

    private String productCreatedTopic= "product-created";

    @Value("${kafka.topic.product.updated}")
    private String productUpdatedTopic;

    @Value("${kafka.topic.product.deleted}")
    private String productDeletedTopic;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate; // Use your event DTO here

    public void sendProductCreatedEvent(OrderDTO product) {
        kafkaTemplate.send(productCreatedTopic, product);
        System.out.println("✅ Sent product created event for product ID: " + product.getId());
    }

    public void sendProductUpdatedEvent(OrderDTO product) {
        kafkaTemplate.send(productUpdatedTopic, product);
        System.out.println("✅ Sent product updated event for product ID: " + product.getId());
    }

    public void sendProductDeletedEvent(String productId) {
        // You might want to create a specific DTO for deleted events
        OrderDTO product = new OrderDTO();
        product.setId(productId);
        kafkaTemplate.send(productDeletedTopic, productId, product);
        System.out.println("✅ Sent product deleted event for product ID: " + productId);
    }
}