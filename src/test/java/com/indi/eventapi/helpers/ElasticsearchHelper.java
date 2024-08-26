package com.indi.eventapi.helpers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.exception.ElasticsearchIOException;
import com.indi.eventapi.models.StockUpdate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@AllArgsConstructor
@Service
@Slf4j
public class ElasticsearchHelper {

    private ElasticsearchClient esClient;

    private ObjectMapper mapper;


    public void deleteAllDocs() {
        try {
            esClient.delete(s -> s.index("stock_*"));
        } catch (IOException e) {
            throw new ElasticsearchIOException(e);
        }
    }

    public Optional<StockUpdate> searchAll() {
        try {
            var response = esClient.search(s -> s
                            .index("stock_updates_1")
                            .size(1)
                            .query(q -> q.matchAll(t -> t)),
                    StockUpdate.class);

            if (isNull(response.hits().total()) || response.hits().total().value() == 0) {
                return Optional.empty();
            }
            return response.hits().hits()
                    .stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(hit -> mapper.convertValue(hit, StockUpdate.class));
        } catch (IOException e) {
            throw new ElasticsearchIOException(e);
        }
    }
}
