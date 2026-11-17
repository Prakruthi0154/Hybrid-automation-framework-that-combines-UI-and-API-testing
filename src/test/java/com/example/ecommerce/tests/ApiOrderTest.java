package com.example.ecommerce.tests;

import com.example.ecommerce.api.models.AuthResponse;
import com.example.ecommerce.api.models.CartRequest;
import com.example.ecommerce.api.models.LoginRequest;
import com.example.ecommerce.api.models.OrderRequest;
import com.example.ecommerce.api.models.UserRequest;
import com.example.ecommerce.base.BaseApiTest;
import com.example.ecommerce.config.ConfigReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class ApiOrderTest extends BaseApiTest {
    @Test(groups = {"api"})
    public void addCartCreateOrderAndReadOrder() {
        String email = "order+" + UUID.randomUUID() + "@example.test";
        String password = "OrderTest-" + UUID.randomUUID() + "-9!";
        Response created = api.createUser(new UserRequest(email, password));
        Assert.assertTrue(created.statusCode() == 201 || created.statusCode() == 200,
                "Create user returned HTTP " + created.statusCode());

        Response login = api.login(new LoginRequest(email, password));
        Assert.assertEquals(login.statusCode(), 200);
        AuthResponse auth = login.as(AuthResponse.class);
        Assert.assertNotNull(auth.token(), "Auth token is missing");
        String productId = ConfigReader.require("api.test.product.id");
        Response cart = api.addToCart(auth.token(), new CartRequest(productId, 1));
        Assert.assertTrue(cart.statusCode() == 200 || cart.statusCode() == 201,
                "Add to cart returned HTTP " + cart.statusCode());
        String cartId = cart.jsonPath().getString("id");
        if (cartId == null) cartId = cart.jsonPath().getString("cartId");
        Assert.assertNotNull(cartId, "Cart response did not contain id/cartId");

        Response order = api.createOrder(auth.token(), new OrderRequest(cartId));
        Assert.assertTrue(order.statusCode() == 200 || order.statusCode() == 201,
                "Create order returned HTTP " + order.statusCode());
        String orderId = order.jsonPath().getString("id");
        if (orderId == null) orderId = order.jsonPath().getString("orderId");
        Assert.assertNotNull(orderId, "Order response did not contain id/orderId");

        Response retrieved = api.getOrder(auth.token(), orderId);
        Assert.assertEquals(retrieved.statusCode(), 200);
        String retrievedId = retrieved.jsonPath().getString("id");
        if (retrievedId == null) retrievedId = retrieved.jsonPath().getString("orderId");
        Assert.assertEquals(retrievedId, orderId, "Retrieved order does not match created order");
    }
}
