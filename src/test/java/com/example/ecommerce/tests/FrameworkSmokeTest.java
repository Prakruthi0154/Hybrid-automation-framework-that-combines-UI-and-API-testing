package com.example.ecommerce.tests;

import com.example.ecommerce.api.models.CartRequest;
import com.example.ecommerce.api.models.OrderRequest;
import com.example.ecommerce.config.ConfigReader;
import com.example.ecommerce.utilities.ExcelReader;
import com.example.ecommerce.utilities.JsonReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FrameworkSmokeTest {
    @Test(groups = "framework")
    public void defaultConfigurationContainsFrameworkSettings() {
        Assert.assertEquals(ConfigReader.get("env"), "qa");
        Assert.assertNotNull(ConfigReader.get("base.url"));
        Assert.assertNotNull(ConfigReader.get("api.base.url"));
        Assert.assertTrue(ConfigReader.getInt("explicit.wait.seconds", 0) > 0);
        Assert.assertTrue(ConfigReader.getInt("api.connect.timeout.ms", 0) > 0);
        Assert.assertNotNull(ConfigReader.get("ui.login.email"));
        Assert.assertNotNull(ConfigReader.get("api.orders.path"));
    }

    @Test(groups = "framework")
    public void bundledJsonAndExcelTestDataCanBeRead() throws IOException {
        String query = JsonReader.readResource("testdata/users.json")
                .path("productSearch").path("query").asText();
        Assert.assertFalse(query.isBlank(), "JSON product search test data is missing");

        List<Map<String, String>> users = ExcelReader.readResource("testdata/test-users.xlsx", "users");
        Assert.assertFalse(users.isEmpty(), "Excel user test data is missing");
        Assert.assertTrue(users.get(0).containsKey("email"));
        Assert.assertTrue(users.get(0).containsKey("password"));
    }

    @Test(groups = "framework", expectedExceptions = IllegalArgumentException.class)
    public void cartRequestRejectsInvalidQuantity() {
        new CartRequest("test-product", 0);
    }

    @Test(groups = "framework", expectedExceptions = IllegalArgumentException.class)
    public void orderRequestRejectsMissingCartId() {
        new OrderRequest(" ");
    }
}
