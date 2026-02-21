package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class UpcomingAlbumsFetcherTest {

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
    @DisplayName("Verifies that fetching the upcoming album data works")
    void givenAlbumFetcherWhenFetchingDataThenShouldReturnExpectedValue() throws Exception {
        String json = """
        {
        "aaData": [
            [
            "<a href=\\"https://band\\">Test Band</a>",
            "<a href=\\"https://album\\">Test Album</a>",
            "Full-length",
            "Black Metal"
            ]
        ]
        }
        """;
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/html; charset=utf-8")
                .setBody(json)
        );

        String url = server.url("/test").toString();

        UpcomingAlbumsFetcher fetcher = new UpcomingAlbumsFetcher();

        String doc = fetcher.fetch(url);

        assertTrue(doc.contains("Test Band"));
    }
}

