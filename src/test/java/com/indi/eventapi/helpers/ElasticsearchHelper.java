package com.indi.eventapi.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.dto.UserUpdateDto;
import exception.ElasticsearchIOException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;

@Service
@Slf4j
public class ElasticsearchHelper {

    @Autowired
    private RestHighLevelClient esClient;

    @Autowired
    private ObjectMapper mapper;


    public void deleteAllDocs() {
        DeleteIndexRequest request = new DeleteIndexRequest("user_*");
        try {
            esClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Delete request failed {}", e.getMessage());
        }
    }

    public Optional<UserUpdateDto> findById(String id) {
        GetRequest request = new GetRequest("user_updates_1", id);
        try{
            var response = esClient.get(request, RequestOptions.DEFAULT);
            if (!response.isExists()) {
                return empty();
            }
            return Optional.of(mapper.readValue(response.getSourceAsString(), UserUpdateDto.class));
        } catch (IOException e) {
            throw new ElasticsearchIOException(e);
        }
    }
}
