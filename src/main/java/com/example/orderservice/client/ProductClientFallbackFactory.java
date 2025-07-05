package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallbackFactory implements ProductClient {

    @Override
    public ProductDTO getProductById(String id) {
        ProductDTO fallback = new ProductDTO();
        fallback.setId(id);
        fallback.setName("Unavailable Product");
        fallback.setDescription("This product is currently unavailable.");
        fallback.setPrice(0.0);
        return fallback;
    }
}
