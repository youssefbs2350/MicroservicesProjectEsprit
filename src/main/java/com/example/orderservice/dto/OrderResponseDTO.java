package com.example.orderservice.dto;

public class OrderResponseDTO {
    private Long id;
    private String productId;
    private int quantity;
    private double price;
    private String customerName;
    private ProductDTO product;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }
}
