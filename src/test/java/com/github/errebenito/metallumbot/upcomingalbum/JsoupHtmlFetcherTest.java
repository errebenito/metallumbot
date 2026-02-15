package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class JsoupHtmlFetcherTest {

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
    void shouldFetchHtmlDocument() throws Exception {

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/html; charset=utf-8")
                .setBody("<html><body><h1>Hello</h1></body></html>")
        );

        String url = server.url("/test").toString();

        JsoupHtmlFetcher fetcher = new JsoupHtmlFetcher();

        Document doc = fetcher.fetch(url);

        assertEquals("Hello", doc.selectFirst("h1").text());
    }
}

