package com.example.ecommerce.tests;

import com.example.ecommerce.base.BaseUiTest;
import com.example.ecommerce.config.ConfigReader;
import com.example.ecommerce.dataproviders.TestDataProviders;
import com.example.ecommerce.pages.CartPage;
import com.example.ecommerce.pages.HomePage;
import com.example.ecommerce.pages.LoginPage;
import org.testng.annotations.Test;

import java.util.Map;

public class UiCartLifecycleTest extends BaseUiTest {
    @Test(groups = {"ui"}, dataProvider = "excelUsers", dataProviderClass = TestDataProviders.class)
    public void loginUpdateRemoveCartAndLogout(Map<String, String> user) {
        String email = required(user, "email");
        String password = required(user, "password");
        driver().get(ConfigReader.require("base.url"));
        new LoginPage(driver()).login(email, password);

        HomePage home = new HomePage(driver());
        home.search("wireless headphones");
        home.addFirstProductToCart();
        home.openCart();
        CartPage cart = new CartPage(driver());
        cart.updateFirstQuantity("2");
        cart.removeFirstItem();
        home.logout();
    }

    private String required(Map<String, String> row, String column) {
        String value = row.get(column);
        if (value == null || value.isBlank() || value.startsWith("replace-with-")) {
            throw new IllegalArgumentException("Set a valid '" + column + "' in testdata/test-users.xlsx");
        }
        return value;
    }
}
