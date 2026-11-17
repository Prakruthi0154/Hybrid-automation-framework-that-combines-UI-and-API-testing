package com.example.ecommerce.api;

import com.example.ecommerce.api.models.CartRequest;
import com.example.ecommerce.api.models.LoginRequest;
import com.example.ecommerce.api.models.OrderRequest;
import com.example.ecommerce.api.models.UserRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class EcommerceApi {
    public Response createUser(UserRequest user) {
        return given().spec(ApiUtility.requestSpecification()).body(user)
                .post(com.example.ecommerce.config.ConfigReader.require("api.users.path"))
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }

    public Response login(LoginRequest credentials) {
        return given().spec(ApiUtility.requestSpecification()).body(credentials)
                .post(com.example.ecommerce.config.ConfigReader.require("api.login.path"))
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }

    public Response getUser(String token, String userId) {
        return given().spec(ApiUtility.requestSpecification()).auth().oauth2(token)
                .get(com.example.ecommerce.config.ConfigReader.require("api.users.path") + "/" + userId)
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }

    public Response addToCart(String token, CartRequest cart) {
        return given().spec(ApiUtility.requestSpecification()).auth().oauth2(token).body(cart)
                .post(com.example.ecommerce.config.ConfigReader.require("api.cart.path"))
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }

    public Response createOrder(String token, OrderRequest order) {
        return given().spec(ApiUtility.requestSpecification()).auth().oauth2(token).body(order)
                .post(com.example.ecommerce.config.ConfigReader.require("api.orders.path"))
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }

    public Response getOrder(String token, String orderId) {
        return given().spec(ApiUtility.requestSpecification()).auth().oauth2(token)
                .get(com.example.ecommerce.config.ConfigReader.require("api.orders.path") + "/" + orderId)
                .then().spec(ApiUtility.successfulJsonResponse()).extract().response();
    }
}
