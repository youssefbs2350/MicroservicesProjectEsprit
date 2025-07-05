package com.example.orderservice.controller;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.dto.ProductDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Qualifier("com.example.orderservice.client.ProductClient")
    @Autowired
    private ProductClient productClient;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrderResponseDTO createOrder(@RequestParam String productId,
                                        @RequestParam String customerName,
                                        @RequestParam int quantity) {
        return orderService.createOrder(productId, customerName, quantity);
    }


    @GetMapping("/test-product/{id}")
    public ProductDTO testProductFeign(@PathVariable String id) {
        return productClient.getProductById(id);
    }
    @GetMapping("/test-product-fallback/{id}")
    public ProductDTO testProductFeignFallback(@PathVariable String id) {
        return productClient.getProductById(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    existingOrder.setProductId(order.getProductId());
                    existingOrder.setQuantity(order.getQuantity());
                    existingOrder.setPrice(order.getPrice());
                    existingOrder.setCustomerName(order.getCustomerName());
                    Order updatedOrder = (Order) orderService.save(existingOrder);
                    return ResponseEntity.ok(updatedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrderById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
