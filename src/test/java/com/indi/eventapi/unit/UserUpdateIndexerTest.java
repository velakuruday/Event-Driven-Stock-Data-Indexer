package com.indi.eventapi.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.StockDto;
import com.indi.eventapi.dto.StockUpdateDto;
import com.indi.eventapi.service.UserUpdateIndexer;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.shard.ShardId;
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

    private RestHighLevelClient esClient;

    private UserUpdateIndexer userUpdateIndexer;

    @Before
    public void setUp() {
        esClient = mock(RestHighLevelClient.class);
        mapper = mock(ObjectMapper.class);
        userUpdateIndexer = new UserUpdateIndexer(esClient, mapper);
    }

    @Test
    public void testSuccessfulIndexing() throws IOException {

        var message = parseJson("src/test/resources/user_update_test_data.json");

        var update = StockUpdateDto.builder()
                .timestamp("2024-08-16 09:30:00-04:00")
                .stocks(List.of(StockDto.builder()
                        .code("AAPL")
                        .adjClose(223.8800048828125F)
                        .close(223.8800048828125F)
                        .high(224.5F)
                        .low(223.6501007080078F)
                        .open(223.9199981689453F)
                        .volume(5272541)
                        .build())).build();

        var response = getTestResponse();

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(StockUpdateDto.class))).thenReturn(update);

        when(esClient.index(Mockito.any(), Mockito.any())).thenReturn(response);

        userUpdateIndexer.indexUserUpdate(message, ack);

        verify(mapper.writeValueAsString(eq(update)));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testJsonParseException() throws IOException {
        var message = parseJson("src/test/resources/user_update_test_data.json");

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(StockUpdateDto.class))).thenThrow(JsonProcessingException.class);

        userUpdateIndexer.indexUserUpdate(message, ack);

        verify(ack, times(1)).acknowledge();
    }

    private String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }

    private IndexResponse getTestResponse() {
        var response = new IndexResponse.Builder();
        response.setResult(DocWriteResponse.Result.CREATED);
        response.setId("1");
        response.setType("_doc");
        response.setShardId(new ShardId("stock_updates_1", "1", -1));
        response.setVersion(1L);

        return response.build();
    }
}