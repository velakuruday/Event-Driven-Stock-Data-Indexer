package com.indi.eventapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class UserUpdateIndexer {
    private final RestHighLevelClient esClient;

    private final ObjectMapper objectMapper;

    private final String ELASTIC_SEARCH_INDEX_VERSION = "1";

    public UserUpdateIndexer(RestHighLevelClient esClient, ObjectMapper objectMapper) {
        this.esClient = esClient;
        this.objectMapper = objectMapper;
    }

    public void indexUserUpdate(String message, Acknowledgment ack) {
        try {
            IndexRequest request = new IndexRequest("user_updates_" + ELASTIC_SEARCH_INDEX_VERSION)
                    .source(message, XContentType.JSON);

            esClient.index(request, RequestOptions.DEFAULT);
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Error processing message {}", e.getMessage());
            ack.acknowledge();
        } catch (IOException e) {
            log.error("Indexing failed {}", e.getMessage());
        }
    }
}
