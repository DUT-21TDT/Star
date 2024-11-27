package com.pbl.star.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonConfig {

    public ObjectMapper queueObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule for handling Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
