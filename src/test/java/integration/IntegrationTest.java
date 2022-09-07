package integration;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@DirtiesContext
@Slf4j
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class IntegrationTest {

    @Autowired
    protected RestHighLevelClient esClient;

    @Autowired
    protected KafkaTemplate<String, String> template;

    @AfterEach
    public void tearDown() {
        DeleteIndexRequest request = new DeleteIndexRequest("*");
        try {
            esClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Delete request failed {}", e.getMessage());
        }
    }

    public String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}
