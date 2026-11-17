package com.example.ecommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("config.properties is missing from the classpath");
            }
            PROPERTIES.load(stream);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null) return override;
        String envOverride = System.getenv("E2E_" + key.toUpperCase().replace('.', '_'));
        if (envOverride != null) return envOverride;
        String environment = System.getProperty("env", PROPERTIES.getProperty("env", "qa"));
        String environmentValue = System.getProperty(environment + "." + key);
        if (environmentValue == null) environmentValue = PROPERTIES.getProperty(environment + "." + key);
        return environmentValue != null ? environmentValue : PROPERTIES.getProperty(key);
    }

    public static String require(String key) {
        String value = get(key);
        if (value == null || value.isBlank() || value.contains("your-store.example") || value.contains("your-api.example")
                || value.contains("replace-with-")) {
            throw new IllegalStateException("Set a valid value for configuration property: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Integer.parseInt(value.trim());
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value.trim());
    }
}
