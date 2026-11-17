package com.example.ecommerce.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) { super(driver); }

    public void login(String email, String password) {
        type("ui.login.email", email);
        type("ui.login.password", password);
        click("ui.login.submit");
    }
}
