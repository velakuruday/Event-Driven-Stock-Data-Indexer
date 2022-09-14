package com.indi.eventapi.integration;

import com.indi.eventapi.configuration.ElasticsearchTestConfig;
import com.indi.eventapi.configuration.KafkaTestUtils;
import com.indi.eventapi.helpers.ElasticsearchHelper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@RunWith(SpringRunner.class)
@Import({ElasticsearchTestConfig.class, KafkaTestUtils.class})
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public abstract class IntegrationTest {

    @Autowired
    protected ElasticsearchHelper elasticsearchHelper;

    @Autowired
    protected KafkaProducer<String, String> producer;

    @AfterEach
    void tearDown() {
        elasticsearchHelper.deleteAllDocs();
    }

    protected String parseJson(String filePath) throws IOException {
        Path fileName = Path.of(filePath);
        return Files.readString(fileName);
    }
}
