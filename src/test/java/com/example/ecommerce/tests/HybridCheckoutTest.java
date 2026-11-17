package com.example.ecommerce.tests;

import com.example.ecommerce.api.EcommerceApi;
import com.example.ecommerce.api.models.AuthResponse;
import com.example.ecommerce.api.models.LoginRequest;
import com.example.ecommerce.api.models.UserRequest;
import com.example.ecommerce.config.ConfigReader;
import com.example.ecommerce.pages.CartPage;
import com.example.ecommerce.pages.CheckoutPage;
import com.example.ecommerce.pages.HomePage;
import com.example.ecommerce.pages.LoginPage;
import com.example.ecommerce.utilities.DriverFactory;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class HybridCheckoutTest {
    private final EcommerceApi api = new EcommerceApi();

    @Test(groups = {"e2e", "api", "ui"})
    public void apiProvisionedUserCanCheckoutAndOrderIsVisibleInApi() {
        String email = "automation+" + UUID.randomUUID() + "@example.test";
        String password = "TestOnly-" + UUID.randomUUID() + "-9!";

        Response createResponse = api.createUser(new UserRequest(email, password));
        Assert.assertTrue(createResponse.statusCode() == 201 || createResponse.statusCode() == 200,
                "API user creation failed: HTTP " + createResponse.statusCode());

        Response loginResponse = api.login(new LoginRequest(email, password));
        Assert.assertEquals(loginResponse.statusCode(), 200, "API login failed");
        AuthResponse auth = loginResponse.as(AuthResponse.class);
        Assert.assertNotNull(auth.token(), "Login response did not contain an auth token");
        Assert.assertFalse(auth.token().isBlank(), "Auth token must not be blank");

        if (auth.userId() != null && !auth.userId().isBlank()) {
            Response userResponse = api.getUser(auth.token(), auth.userId());
            Assert.assertEquals(userResponse.statusCode(), 200, "Could not retrieve API-created user");
            Assert.assertTrue(userResponse.asString().contains(email), "User details do not match created user");
        }

        WebDriver driver = DriverFactory.createDriver();
        driver.get(ConfigReader.require("base.url"));
        new LoginPage(driver).login(email, password);
        HomePage home = new HomePage(driver);
        home.search("wireless headphones");
        home.addFirstProductToCart();
        home.openCart();
        new CartPage(driver).checkout();

        String orderNumber = new CheckoutPage(driver).placeOrderAndGetNumber();
        Assert.assertFalse(orderNumber.isBlank(), "UI checkout did not produce an order number");

        Response orderResponse = api.getOrder(auth.token(), orderNumber);
        Assert.assertEquals(orderResponse.statusCode(), 200, "UI-created order was not found through the API");
        String apiOrderId = orderResponse.jsonPath().getString("id");
        String apiOrderNumber = orderResponse.jsonPath().getString("orderNumber");
        if (apiOrderNumber == null) apiOrderNumber = orderResponse.jsonPath().getString("number");
        Assert.assertTrue(orderNumber.equals(apiOrderId) || orderNumber.equals(apiOrderNumber),
                "API order identifier does not match UI order number " + orderNumber);
    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        DriverFactory.quitDriver();
    }
}
