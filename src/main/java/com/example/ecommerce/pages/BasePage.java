package com.example.ecommerce.pages;

import com.example.ecommerce.utilities.WaitUtility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WaitUtility wait;
    @FindBy(css = "body")
    private WebElement pageBody;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtility(driver);
        PageFactory.initElements(driver, this);
    }

    protected By selector(String key) {
        String selector = com.example.ecommerce.config.ConfigReader.get(key);
        if (selector == null || selector.isBlank()) throw new IllegalStateException("Missing UI selector property: " + key);
        return By.cssSelector(selector);
    }

    protected void type(String key, String value) {
        WebElement element = wait.visible(selector(key));
        element.clear();
        element.sendKeys(value);
    }

    protected void click(String key) {
        wait.clickable(selector(key)).click();
    }
}
