package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(
        name = "product-service",
        path = "/api/products",
        url = "http://localhost:8082",
        fallback = ProductClientFallbackFactory.class

)
public interface ProductClient {
    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable("id") String id);
}
