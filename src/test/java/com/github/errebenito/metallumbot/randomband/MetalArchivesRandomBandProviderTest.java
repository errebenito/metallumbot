package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    void shouldReturnLocationHeaderWhenStatusIs302() throws Exception {

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
    void shouldThrowWhenStatusIsNot302() {
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
    void shouldThrowWhenLocationHeaderMissing() {

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
    void shouldInstantiateNoArgConstructor() {
        MetalArchivesRandomBandProvider provider = new MetalArchivesRandomBandProvider();
        assertNotNull(provider);
    }
}
