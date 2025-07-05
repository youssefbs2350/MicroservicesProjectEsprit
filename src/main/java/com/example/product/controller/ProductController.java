package com.example.product.controller;

import com.example.product.dto.OrderDTO;
import com.example.product.model.Product;
import com.example.product.service.OrderIntegrationService;
import com.example.product.service.ProductEventProducer;
import com.example.product.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderIntegrationService orderIntegrationService;

    @GetMapping("/test-order/{id}")
    public Object testOrderService(@PathVariable Long id) {
        return orderIntegrationService.getOrder(id);
    }
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductEventProducer productEventProducer; // Inject the producer

    @PostMapping
    public OrderDTO createProduct(@RequestBody OrderDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productService.save(product);
        OrderDTO savedProductDTO = modelMapper.map(savedProduct, OrderDTO.class);
        productEventProducer.sendProductCreatedEvent(savedProductDTO); // Send Kafka event
        return savedProductDTO;
    }

    @GetMapping
    public List<OrderDTO> getAllProducts() {
        return productService.findAll().stream()
                .map(product -> modelMapper.map(product, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getProductById(@PathVariable String id) {
        return productService.findById(id)
                .map(product -> modelMapper.map(product, OrderDTO.class))
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteById(id);
    }

    @PutMapping("/{id}")
    public OrderDTO updateProduct(@PathVariable String id, @RequestBody OrderDTO productDTO) {
        Product existingProduct = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Map incoming DTO to existing product entity to update fields
        modelMapper.map(productDTO, existingProduct);
        existingProduct.setId(id); // ensure id stays the same

        Product updatedProduct = productService.save(existingProduct);
        OrderDTO updatedProductDTO = modelMapper.map(updatedProduct, OrderDTO.class);
        productEventProducer.sendProductUpdatedEvent(updatedProductDTO); // Send Kafka event
        return updatedProductDTO;
    }
}
