package com.indi.eventapi.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserUpdateIndexer {

    private String CONSUMER_INDEX = "1";

    private String ELASTIC_SEARCH_INDEX_VERSION = "1";

    private ElasticsearchClient esclient;

    private ObjectMapper objectMapper;

    public void indexUserUpdate(String message, Acknowledgment ack) {
        try {
            UserUpdateDto userUpdate = objectMapper.readValue(message, UserUpdateDto.class);

            log.info("Processed subscription update of user: {}", userUpdate.getName());
        } catch (JsonProcessingException e) {
            log.error("Error processing message {}", e.getMessage());
        }
    }
}
