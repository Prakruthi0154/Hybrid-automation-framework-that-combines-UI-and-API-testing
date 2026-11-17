package com.example.ecommerce.api;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SafeApiLoggingFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(SafeApiLoggingFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification request, FilterableResponseSpecification response,
                           FilterContext context) {
        long start = System.nanoTime();
        String uri = request.getURI().split("\\?", 2)[0];
        LOGGER.info("API request: {} {}", request.getMethod(), uri);
        Response result = context.next(request, response);
        LOGGER.info("API response: {} {} ({} ms)", result.statusCode(), uri,
                (System.nanoTime() - start) / 1_000_000);
        return result;
    }
}
