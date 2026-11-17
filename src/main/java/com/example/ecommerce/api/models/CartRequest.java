package com.example.ecommerce.api.models;

public record CartRequest(String productId, int quantity) {
    public CartRequest {
        if (productId == null || productId.isBlank()) throw new IllegalArgumentException("productId must not be blank");
        if (quantity < 1) throw new IllegalArgumentException("quantity must be at least one");
    }
}
