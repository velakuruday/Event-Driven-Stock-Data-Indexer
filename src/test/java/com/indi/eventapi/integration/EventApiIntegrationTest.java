package com.indi.eventapi.integration;

import com.indi.eventapi.models.Company;
import com.indi.eventapi.models.Stock;
import com.indi.eventapi.models.StockUpdate;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.FIVE_SECONDS;
import static org.awaitility.Durations.TWO_HUNDRED_MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class EventApiIntegrationTest extends IntegrationTest {

    @Test
    public void testSuccessfulIndex() throws IOException {
        String message = parseJson("src/test/resources/user_update_test_data.json");

        var stockUpdate = StockUpdate.builder()
                .timestamp("2024-08-16 09:30:00-04:00")
                .stocks(List.of(Stock.builder()
                        .code("AAPL")
                        .company(Company.codeMap.get("AAPL"))
                        .adjClose(223.8800048828125F)
                        .close(223.8800048828125F)
                        .high(224.5F)
                        .low(223.6501007080078F)
                        .open(223.9199981689453F)
                        .volume(5272541)
                        .build())).build();

        template.send("stock-updates", message);

        elasticsearchHelper.refreshIndices();

        await().atLeast(TWO_HUNDRED_MILLISECONDS)
                .atMost(FIVE_SECONDS)
                .with()
                .pollDelay(TWO_HUNDRED_MILLISECONDS)
                .pollInterval(TWO_HUNDRED_MILLISECONDS)
                .until(() -> elasticsearchHelper.checkCount("stock_updates_1") > 0);

        assertEquals(elasticsearchHelper.searchAll().orElseGet(null), stockUpdate);
    }
}
