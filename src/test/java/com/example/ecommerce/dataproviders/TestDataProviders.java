package com.example.ecommerce.dataproviders;

import com.example.ecommerce.utilities.ExcelReader;
import com.example.ecommerce.utilities.JsonReader;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class TestDataProviders {
    private TestDataProviders() {}

    @DataProvider(name = "jsonSearchTerms")
    public static Object[][] jsonSearchTerms() throws IOException {
        String query = JsonReader.readResource("testdata/users.json").path("productSearch").path("query").asText();
        if (query.isBlank()) throw new IllegalStateException("productSearch.query must be set in testdata/users.json");
        return new Object[][]{{query}};
    }

    @DataProvider(name = "excelUsers")
    public static Object[][] excelUsers() throws IOException {
        List<Map<String, String>> rows = ExcelReader.readResource("testdata/test-users.xlsx", "users");
        return rows.stream().map(row -> new Object[]{row}).toArray(Object[][]::new);
    }
}
