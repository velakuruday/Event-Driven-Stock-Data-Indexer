package integration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

@SpringBootTest
@DirtiesContext
@AllArgsConstructor
@Slf4j
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9091", "port=9091"})
public class IntegrationTest {

    private final RestHighLevelClient esClient;

    @BeforeEach
    public void setUp() {
        DeleteIndexRequest request = new DeleteIndexRequest("user_*");
        try {
            esClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Delete request failed {}", e.getMessage());
        }
    }

    @AfterAll
    public void tearDown() {
        DeleteIndexRequest request = new DeleteIndexRequest("*");
        try {
            esClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Delete request failed {}", e.getMessage());
        }
    }
}
