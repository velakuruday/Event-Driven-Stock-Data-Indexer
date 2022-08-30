package com.indi.eventapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ObjectMapperConfig extends MappingJackson2HttpMessageConverter {

    public ObjectMapperConfig(ObjectMapper objectMapper) {
        super(objectMapper);
        objectMapper.registerModule(new JsonNullableModule());
    }
}

