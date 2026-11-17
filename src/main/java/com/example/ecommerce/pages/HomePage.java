package com.example.ecommerce.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) { super(driver); }

    public void search(String query) {
        type("ui.search", query);
        click("ui.search.submit");
    }

    public void addFirstProductToCart() {
        wait.visible(selector("ui.product.card"));
        click("ui.product.add");
    }

    public void openCart() { click("ui.cart.link"); }
    public void logout() { click("ui.logout"); }
}
