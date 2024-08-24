package com.indi.eventapi.integration;

import com.indi.eventapi.dto.StockUpdateDto;
import com.indi.eventapi.dto.UserUpdateDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.awaitility.Durations;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.Test;

import java.io.IOException;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_SECONDS;
import static org.hamcrest.Matchers.equalTo;

public class EventApiIntegrationTest extends IntegrationTest {

    @Test
    public void testSuccessfulIndex() throws IOException {
        String message = parseJson("src/test/resources/user_update_test_data.json");
        ProducerRecord<String, String> record = new ProducerRecord<>("user-updates", message);

        producer.send(record);

        await().atLeast(Durations.TWO_HUNDRED_MILLISECONDS)
                .atMost(FIVE_SECONDS)
                .with()
                .pollDelay(Durations.TWO_HUNDRED_MILLISECONDS)
                .pollInterval(Durations.TWO_HUNDRED_MILLISECONDS)
                .ignoreException(ElasticsearchStatusException.class)
                .until(() -> elasticsearchHelper.findById("1"
                ).map(StockUpdateDto).orElse(null), equalTo("1"));
    }
}
