package com.indi.eventapi.integration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.indi.eventapi.configuration.ElasticsearchTestConfig;
import com.indi.eventapi.configuration.KafkaTestUtils;
import com.indi.eventapi.helpers.ElasticsearchHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({ElasticsearchTestConfig.class, KafkaTestUtils.class})
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}, kraft = false)
public abstract class IntegrationTest {

    @Autowired
    protected ElasticsearchHelper elasticsearchHelper;
    @Autowired
    protected KafkaTemplate<String, String> template;

    @Autowired
    protected ElasticsearchClient esClient;

    @AfterEach
    void tearDown() {
        elasticsearchHelper.deleteAllDocs();
    }

    protected String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}
