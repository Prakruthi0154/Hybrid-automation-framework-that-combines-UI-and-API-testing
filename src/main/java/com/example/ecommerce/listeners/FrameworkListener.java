package com.example.ecommerce.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.example.ecommerce.reports.ReportManager;
import com.example.ecommerce.utilities.DriverFactory;
import com.example.ecommerce.utilities.ScreenshotUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Path;

public class FrameworkListener implements ITestListener, ISuiteListener, IAnnotationTransformer {
    private static final Logger LOGGER = LogManager.getLogger(FrameworkListener.class);

    @Override
    public void onStart(ISuite suite) {
        ReportManager.initialize();
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(com.example.ecommerce.listeners.RetryAnalyzer.class);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        ReportManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ReportManager.createTest(result.getMethod().getMethodName());
        LOGGER.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ReportManager.currentTest();
        if (test != null) test.pass("Test passed");
        LOGGER.info("Test passed: {}", result.getName());
        ReportManager.clearTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ReportManager.currentTest();
        if (test != null) test.fail(result.getThrowable());
        LOGGER.error("Test failed: {}", result.getName(), result.getThrowable());
        try {
            WebDriver driver = DriverFactory.getDriver();
            Path screenshot = ScreenshotUtility.capture(driver, result.getName());
            if (test != null) test.addScreenCaptureFromPath(screenshot.toString());
        } catch (IllegalStateException ignored) {
            LOGGER.info("No WebDriver was active for failed test {}", result.getName());
        } catch (Exception e) {
            LOGGER.error("Failed to capture failure screenshot", e);
        } finally {
            ReportManager.clearTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ReportManager.currentTest();
        if (test != null) {
            if (result.getThrowable() == null) test.skip("Test skipped");
            else test.skip(result.getThrowable());
        }
        ReportManager.clearTest();
    }
}
