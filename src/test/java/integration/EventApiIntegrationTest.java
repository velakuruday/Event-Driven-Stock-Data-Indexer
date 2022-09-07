package integration;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EventApiIntegrationTest extends IntegrationTest {

    @Test
    public void testSuccessfulIndex() throws IOException {
        String message = parseJson("src/test/resources/user_update_test_data.json");

        template.send("user-updates-1", message);

    }
}
