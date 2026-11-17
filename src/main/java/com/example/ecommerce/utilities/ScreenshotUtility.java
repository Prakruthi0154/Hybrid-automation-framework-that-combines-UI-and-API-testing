package com.example.ecommerce.utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtility {
    private ScreenshotUtility() {}

    public static Path capture(WebDriver driver, String testName) throws IOException {
        Path directory = Path.of("target", "screenshots");
        Files.createDirectories(directory);
        String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS"));
        Path destination = directory.resolve(safeName + "-" + timestamp + ".png");
        Files.copy(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath(), destination);
        return destination;
    }
}
