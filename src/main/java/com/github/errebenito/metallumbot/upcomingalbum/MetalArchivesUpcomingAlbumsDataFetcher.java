package com.github.errebenito.metallumbot.upcomingalbum;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MetalArchivesUpcomingAlbumsDataFetcher implements UpcomingAlbumDataFetcher {
    
    private final String url;

    public MetalArchivesUpcomingAlbumsDataFetcher(String url) {
        this.url = url;
    }

    @Override
    public String fetch() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .cookieHandler(new CookieManager())
        .build();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/120.0.0.0 Safari/537.36")
            .header("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.9")
            .GET()
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}

