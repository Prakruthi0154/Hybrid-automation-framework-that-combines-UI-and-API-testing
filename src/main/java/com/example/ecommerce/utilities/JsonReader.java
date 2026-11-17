package com.example.ecommerce.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonReader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonReader() {}

    public static JsonNode readResource(String resourcePath) throws IOException {
        try (InputStream stream = JsonReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) throw new IOException("JSON test data resource not found: " + resourcePath);
            return MAPPER.readTree(stream);
        }
    }
}
