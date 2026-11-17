package com.example.ecommerce.pages;

import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    public CartPage(WebDriver driver) { super(driver); }

    public void updateFirstQuantity(String quantity) {
        type("ui.cart.quantity", quantity);
        driver.findElement(selector("ui.cart.quantity")).sendKeys(org.openqa.selenium.Keys.ENTER);
    }

    public void removeFirstItem() { click("ui.cart.remove"); }
    public void checkout() { click("ui.checkout"); }
}
