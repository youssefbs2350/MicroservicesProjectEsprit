package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.dto.ProductDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Qualifier("com.example.orderservice.client.ProductClient")
    @Autowired
    private ProductClient productClient;

    private static final String PRODUCT_SERVICE = "productService";

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "fallbackProduct")
    public OrderResponseDTO createOrder(String productId, String customerName, int quantity) {

        ProductDTO product = productClient.getProductById(productId);

        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setPrice(product.getPrice());
        order.setCustomerName(customerName);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setProductId(productId);
        response.setQuantity(quantity);
        response.setPrice(product.getPrice());
        response.setCustomerName(customerName);
        response.setProduct(product);

        return response;
    }

    public OrderResponseDTO fallbackProduct(String productId, String customerName, int quantity, Throwable throwable) {
        System.err.println("⚠️ Fallback triggered for productId: " + productId + " - Reason: " + throwable.getMessage());

        ProductDTO product = new ProductDTO(productId, "Unavailable Product", "This product is currently unavailable.", 0.0);

        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setPrice(0.0);
        order.setCustomerName(customerName);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setProductId(productId);
        response.setQuantity(quantity);
        response.setPrice(0.0);
        response.setCustomerName(customerName);
        response.setProduct(product);

        return response;
    }

}
