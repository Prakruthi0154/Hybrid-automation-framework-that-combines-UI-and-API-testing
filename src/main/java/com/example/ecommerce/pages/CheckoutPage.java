package com.example.ecommerce.pages;

import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {
    public CheckoutPage(WebDriver driver) { super(driver); }

    public String placeOrderAndGetNumber() {
        click("ui.place.order");
        wait.visible(selector("ui.order.confirmation"));
        return wait.visible(selector("ui.order.number")).getText().trim();
    }

    public boolean isOrderConfirmationDisplayed() {
        return wait.present(selector("ui.order.confirmation"));
    }
}
