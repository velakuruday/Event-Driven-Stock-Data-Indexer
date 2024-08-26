package com.indi.eventapi.unit;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.StockDataDto;
import com.indi.eventapi.dto.StockUpdateDataDto;
import com.indi.eventapi.models.Company;
import com.indi.eventapi.models.Stock;
import com.indi.eventapi.models.StockUpdate;
import com.indi.eventapi.service.UserUpdateIndexer;
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

public class UserUpdateIndexerTest {

    private ObjectMapper mapper;

    private ElasticsearchClient esClient;

    private UserUpdateIndexer userUpdateIndexer;

    @Before
    public void setUp() {
        esClient = mock(ElasticsearchClient.class);
        mapper = mock(ObjectMapper.class);
        userUpdateIndexer = new UserUpdateIndexer(esClient, mapper);
    }

    @Test
    public void testSuccessfulIndexing() throws IOException {

        var message = parseJson("src/test/resources/user_update_test_data.json");

        var update = StockUpdateDataDto.builder()
                .timestamp("2024-08-16 09:30:00-04:00")
                .stocks(List.of(StockDataDto.builder()
                        .code("AAPL")
                        .adjClose(223.8800048828125F)
                        .close(223.8800048828125F)
                        .high(224.5F)
                        .low(223.6501007080078F)
                        .open(223.9199981689453F)
                        .volume(5272541)
                        .build())).build();

        var stockUpdate = StockUpdate.builder()
                .timestamp("2024-08-16 09:30:00-04:00")
                .stocks(List.of(Stock.builder()
                        .code("AAPL")
                        .company(Company.codeMap.get("AAPL"))
                        .adjClose(223.8800048828125F)
                        .close(223.8800048828125F)
                        .high(224.5F)
                        .low(223.6501007080078F)
                        .open(223.9199981689453F)
                        .volume(5272541)
                        .build())).build();

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(StockUpdateDataDto.class))).thenReturn(update);

        userUpdateIndexer.indexUserUpdate(message, ack);

        verify(esClient).index(any(IndexRequest.class));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testJsonParseException() throws IOException {
        var message = parseJson("src/test/resources/user_update_test_data.json");

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), eq(StockUpdateDataDto.class))).thenThrow(JsonProcessingException.class);

        userUpdateIndexer.indexUserUpdate(message, ack);

        verify(ack, times(1)).acknowledge();
    }

    private String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}