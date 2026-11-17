package com.example.ecommerce.tests;

import com.example.ecommerce.base.BaseUiTest;
import com.example.ecommerce.config.ConfigReader;
import com.example.ecommerce.dataproviders.TestDataProviders;
import com.example.ecommerce.pages.HomePage;
import org.testng.annotations.Test;

public class UiSmokeTest extends BaseUiTest {
    @Test(groups = {"ui"}, dataProvider = "jsonSearchTerms", dataProviderClass = TestDataProviders.class)
    public void searchForProduct(String query) {
        driver().get(ConfigReader.require("base.url"));
        new HomePage(driver()).search(query);
        new HomePage(driver()).addFirstProductToCart();
    }
}
