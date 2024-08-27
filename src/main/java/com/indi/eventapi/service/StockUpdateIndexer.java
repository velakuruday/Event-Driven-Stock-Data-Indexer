package com.indi.eventapi.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.StockUpdateDto;
import com.indi.eventapi.models.StockUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class StockUpdateIndexer {
    private final ElasticsearchClient esClient;

    private final ObjectMapper objectMapper;

    private final String ELASTIC_SEARCH_INDEX_VERSION = "1";

    public StockUpdateIndexer(ElasticsearchClient esClient, ObjectMapper objectMapper) {
        this.esClient = esClient;
        this.objectMapper = objectMapper;
    }

    public void indexUserUpdate(String message, Acknowledgment ack) {
        try {
            StockUpdateDto stockUpdateDto = objectMapper.readValue(message, StockUpdateDto.class);

            IndexRequest<StockUpdate> request = IndexRequest.of(i -> i
                    .index("stock_updates_" + ELASTIC_SEARCH_INDEX_VERSION)
                    .document(StockUpdate.toStockUpdate(stockUpdateDto)));

            esClient.index(request);
        } catch (JsonProcessingException e) {
            log.error("Error processing message {}", e.getMessage());
        } catch (IOException e) {
            log.error("Indexing failed {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }
}
