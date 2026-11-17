package com.example.ecommerce.base;

import com.example.ecommerce.utilities.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {
    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DriverFactory.createDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        DriverFactory.quitDriver();
    }
}
