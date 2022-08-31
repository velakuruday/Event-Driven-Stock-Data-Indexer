package com.indi.eventapi.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.UserUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserUpdateIndexer {
    private final ElasticsearchClient esClient;

    private final ObjectMapper objectMapper;

    private final String CONSUMER_INDEX = "1";

    private final String ELASTIC_SEARCH_INDEX_VERSION = "1";

    public UserUpdateIndexer(ElasticsearchClient esClient, ObjectMapper objectMapper) {
        this.esClient = esClient;
        this.objectMapper = objectMapper;
    }

    public void indexUserUpdate(String message, Acknowledgment ack) {
        try {
            UserUpdateDto userUpdate = objectMapper.readValue(message, UserUpdateDto.class);

            log.info("Processed subscription update of user: {}", userUpdate.getName());
        } catch (JsonProcessingException e) {
            log.error("Error processing message {}", e.getMessage());
        }
    }
}
