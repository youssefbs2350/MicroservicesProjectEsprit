package com.example.orderservice.dto;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventConsumer {

    @KafkaListener(topics = "product.created", groupId = "order-group", containerFactory = "productKafkaListenerFactory")
    public void handleProductCreated(ProductDTO product) {
        System.out.println("📩 Produit reçu depuis Kafka :");
        System.out.println("🆔 ID : " + product.getId());
        System.out.println("🛒 Nom : " + product.getName());
        System.out.println("💵 Prix : " + product.getPrice());
    }
}
