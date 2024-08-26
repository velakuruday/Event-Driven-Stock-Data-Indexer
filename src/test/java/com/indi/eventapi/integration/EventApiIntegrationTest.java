package com.indi.eventapi.integration;

import org.junit.Test;

import java.io.IOException;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_SECONDS;
import static org.awaitility.Durations.TWO_HUNDRED_MILLISECONDS;


public class EventApiIntegrationTest extends IntegrationTest {

    @Test
    public void testSuccessfulIndex() throws IOException {
        String message = parseJson("src/test/resources/user_update_test_data.json");

        template.send("stock-updates", message);

        esClient.indices().refresh(r -> r);

        await().atLeast(TWO_HUNDRED_MILLISECONDS)
                .atMost(FIVE_SECONDS)
                .with()
                .pollDelay(TWO_HUNDRED_MILLISECONDS)
                .pollInterval(TWO_HUNDRED_MILLISECONDS)
                .until(() -> elasticsearchHelper.searchAll().isPresent());
    }
}
