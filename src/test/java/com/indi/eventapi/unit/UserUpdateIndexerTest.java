package com.indi.eventapi.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.UserUpdateDto;
import com.indi.eventapi.dto.UserUpdateMembershipDto;
import com.indi.eventapi.service.UserUpdateIndexer;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.shard.ShardId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.kafka.support.Acknowledgment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

        var update = UserUpdateDto.builder()
                .name("Rick Sanchez")
                .id("1")
                .email("rickc137@rickmail.com")
                .phone(JsonNullable.of("+1 532 545 89520"))
                .membership(UserUpdateMembershipDto.builder().status("premium").category(JsonNullable.of("family")).build())
                .address(JsonNullable.of("Smith Residence, Washington, USA"))
                .build();

        var response = getTestResponse();

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(UserUpdateDto.class))).thenReturn(update);

        when(esClient.index(Mockito.any(),Mockito.any())).thenReturn(response);

        userUpdateIndexer.indexUserUpdate(message, ack);

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testJsonParseException() throws IOException {
        var message = parseJson("src/test/resources/user_update_test_data.json");

        var ack = mock(Acknowledgment.class);

        when(mapper.readValue(anyString(), Mockito.eq(UserUpdateDto.class))).thenThrow(JsonProcessingException.class);

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
        response.setShardId(new ShardId("user_updates_1","1",-1));
        response.setVersion(1L);

        return response.build();
    }
}