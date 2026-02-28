package com.github.errebenito.metallumbot.randomband;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MetalArchivesRandomBandProvider implements RandomBandProvider {
    private final String randomBandUrl;
    private final HttpClient client;

    public MetalArchivesRandomBandProvider(String randomBandUrl,
                                           HttpClient client) {
        this.randomBandUrl = randomBandUrl;
        this.client = client;
    }

    public MetalArchivesRandomBandProvider() {
        this(
            "https://www.metal-archives.com/band/random",
            HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build()
        );
    }

    @Override
    public String getRandomBandUrl() throws IllegalStateException, IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.randomBandUrl))
                .GET()
                .header("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .build();

        HttpResponse<Void> response =
                client.send(request, HttpResponse.BodyHandlers.discarding());

        if (response.statusCode() != 302) {
            throw new IllegalStateException(
                    "Expected 302 but got " + response.statusCode());
        }

        return response.headers()
                .firstValue("Location")
                .orElseThrow(() ->
                        new IllegalStateException("Missing Location header"));
    }
}
