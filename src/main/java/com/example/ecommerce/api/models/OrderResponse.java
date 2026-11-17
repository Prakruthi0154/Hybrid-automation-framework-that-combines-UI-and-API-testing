package com.example.ecommerce.api.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record OrderResponse(@JsonAlias({"id", "orderId"}) String id,
                            @JsonAlias({"orderNumber", "number"}) String orderNumber,
                            String status) {}
