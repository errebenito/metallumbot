package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class MetalArchivesRandomBandProviderTest {

    private MockWebServer server;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    @DisplayName("Verifies that the band link is retrieved from the location header")
    void givenRandomBandRequestWhenResponseHasStatus302AndLocationHeaderThenShouldReturnBandLink() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(302)
                .addHeader("Location",
                        "https://www.metal-archives.com/bands/Test/123"));

        String baseUrl = server.url("/band/random").toString();

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        MetalArchivesRandomBandProvider provider =
                new MetalArchivesRandomBandProvider(baseUrl, client);

        String result = provider.getRandomBandUrl();

        assertEquals(
                "https://www.metal-archives.com/bands/Test/123",
                result
        );
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the response to a random band request does not have the expected status")
    void givenRandomBandRequestWhenResponseHasUnexpectedStatusThenShouldThrow() {
        server.enqueue(new MockResponse()
            .setResponseCode(200));

        String baseUrl = server.url("/band/random").toString();

        HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

        MetalArchivesRandomBandProvider provider =
            new MetalArchivesRandomBandProvider(baseUrl, client);

        assertThrows(IllegalStateException.class,
            provider::getRandomBandUrl);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the response to a random band request does not have the expected header")
    void givenRandomBandRequestWhenResponseHasMissingHeaderThenShouldThrow() {
        server.enqueue(new MockResponse()
            .setResponseCode(302));

        String baseUrl = server.url("/band/random").toString();

        HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

        MetalArchivesRandomBandProvider provider =
            new MetalArchivesRandomBandProvider(baseUrl, client);

        assertThrows(IllegalStateException.class,
            provider::getRandomBandUrl);
    }

    @Test
    @DisplayName("Verifies that the provider is built correctly")
    void givenProviderWhenConstructingThenShouldReturnNotNull() {
        MetalArchivesRandomBandProvider provider = new MetalArchivesRandomBandProvider();
        assertNotNull(provider);
    }
}
