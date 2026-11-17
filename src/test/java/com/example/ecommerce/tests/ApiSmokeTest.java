package com.example.ecommerce.tests;

import com.example.ecommerce.api.models.AuthResponse;
import com.example.ecommerce.api.models.LoginRequest;
import com.example.ecommerce.api.models.UserRequest;
import com.example.ecommerce.base.BaseApiTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class ApiSmokeTest extends BaseApiTest {
    @Test(groups = {"api"})
    public void createLoginAndReadUser() {
        String email = "api+" + UUID.randomUUID() + "@example.test";
        String password = "ApiTest-" + UUID.randomUUID() + "-9!";
        Response created = api.createUser(new UserRequest(email, password));
        Assert.assertTrue(created.statusCode() == 201 || created.statusCode() == 200,
                "Create user returned HTTP " + created.statusCode());

        Response login = api.login(new LoginRequest(email, password));
        Assert.assertEquals(login.statusCode(), 200);
        AuthResponse auth = login.as(AuthResponse.class);
        Assert.assertNotNull(auth.token(), "Auth token is missing");
        Assert.assertNotNull(auth.userId(), "User ID is missing from login response");

        Response user = api.getUser(auth.token(), auth.userId());
        Assert.assertEquals(user.statusCode(), 200);
        Assert.assertTrue(user.asString().contains(email), "Returned user email differs from created user");
    }
}
