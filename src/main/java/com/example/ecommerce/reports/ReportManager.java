package com.example.ecommerce.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ReportManager {
    private static ExtentReports reports;
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    private ReportManager() {}

    public static synchronized void initialize() {
        if (reports != null) return;
        try {
            Path directory = Path.of("target", "extent-reports");
            Files.createDirectories(directory);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            ExtentSparkReporter spark = new ExtentSparkReporter(directory.resolve("execution-" + timestamp + ".html").toString());
            spark.config().setDocumentTitle("E-Commerce Hybrid Automation");
            spark.config().setReportName("Test Execution Summary");
            reports = new ExtentReports();
            reports.attachReporter(spark);
            reports.setSystemInfo("Framework", "E-Commerce Hybrid Automation");
            reports.setSystemInfo("Java", System.getProperty("java.version"));
            reports.setSystemInfo("Browser", System.getProperty("browser", "chrome"));
            reports.setSystemInfo("Environment", System.getProperty("env", "qa"));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to initialize Extent report", e);
        }
    }

    public static ExtentTest createTest(String name) {
        initialize();
        ExtentTest test = reports.createTest(name);
        CURRENT_TEST.set(test);
        return test;
    }

    public static ExtentTest currentTest() {
        return CURRENT_TEST.get();
    }

    public static void clearTest() {
        CURRENT_TEST.remove();
    }

    public static synchronized void flush() {
        if (reports != null) reports.flush();
    }
}
