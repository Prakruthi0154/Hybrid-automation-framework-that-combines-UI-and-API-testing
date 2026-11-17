package com.example.ecommerce.api.models;

public record OrderRequest(String cartId) {
    public OrderRequest {
        if (cartId == null || cartId.isBlank()) throw new IllegalArgumentException("cartId must not be blank");
    }
}
