package com.indi.eventapi.unit;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.StockDto;
import com.indi.eventapi.dto.StockUpdateDto;
import com.indi.eventapi.service.StockUpdateIndexer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.kafka.support.Acknowledgment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StockUpdateIndexerTest {

    private ObjectMapper mapper;

    private ElasticsearchClient esClient;

    private StockUpdateIndexer stockUpdateIndexer;

    @Before
    public void setUp() {
        esClient = mock(ElasticsearchClient.class);
        mapper = mock(ObjectMapper.class);
        stockUpdateIndexer = new StockUpdateIndexer(esClient, mapper);
    }

    @Test
    public void testSuccessfulIndexing() throws IOException {

        var message = parseJson("src/test/resources/stock_update_test_data.json");

        var update = StockUpdateDto.builder()
                .timestamp("2024-08-16 09:30:00-04:00")
                .stockList(List.of(StockDto.builder()
                        .name("AAPL")
                        .adjClose(223.8800048828125F)
                        .close(223.8800048828125F)
                        .high(224.5F)
                        .low(223.6501007080078F)
                        .open(223.9199981689453F)
                        .volume(5272541)
                        .build())).build();

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(StockUpdateDto.class))).thenReturn(update);

        stockUpdateIndexer.indexUserUpdate(message, ack);

        verify(esClient).index(any(IndexRequest.class));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testJsonParseException() throws IOException {
        var message = parseJson("src/test/resources/stock_update_test_data.json");

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), eq(StockUpdateDto.class))).thenThrow(JsonProcessingException.class);

        stockUpdateIndexer.indexUserUpdate(message, ack);

        verify(ack, times(1)).acknowledge();
    }

    private String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}