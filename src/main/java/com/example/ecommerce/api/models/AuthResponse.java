package com.example.ecommerce.api.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AuthResponse(@JsonAlias({"access_token", "accessToken"}) String token,
                           @JsonAlias({"id", "userId"}) String userId) {}
