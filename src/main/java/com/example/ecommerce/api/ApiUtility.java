package com.example.ecommerce.api;

import com.example.ecommerce.config.ConfigReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.http.ContentType.JSON;

public final class ApiUtility {
    private ApiUtility() {}

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.require("api.base.url"))
                .setContentType(JSON)
                .setAccept(JSON)
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeoutMillis("api.connect.timeout.ms", 10000))
                        .setParam("http.socket.timeout", timeoutMillis("api.read.timeout.ms", 20000))))
                .addFilter(new SafeApiLoggingFilter())
                .build();
    }

    public static ResponseSpecification successfulJsonResponse() {
        return new ResponseSpecBuilder().expectContentType(JSON).build();
    }

    public static int timeoutMillis(String key, int fallback) {
        return ConfigReader.getInt(key, fallback);
    }
}
