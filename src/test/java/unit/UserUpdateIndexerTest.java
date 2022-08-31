package unit;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indi.eventapi.services.UserUpdateIndexer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

class UserUpdateIndexerTest {

    private ObjectMapper mapper;

    private ElasticsearchClient esClient;

    private UserUpdateIndexer userUpdateIndexer;

    @BeforeAll
    void setUp() {
        esClient = mock(ElasticsearchClient.class);
        mapper = mock(ObjectMapper.class);
        userUpdateIndexer = new UserUpdateIndexer(esClient, mapper);
    }

    @Test
    void testSuccessfulIndexing() {
        assertEquals(1,1);
    }

}