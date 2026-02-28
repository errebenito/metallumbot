package com.github.errebenito.metallumbot.upcomingalbum;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetalArchivesUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {
    private record CacheEntry(JsonNode data, Instant expiresAt) {

        boolean isExpired(Clock clock) {
            return Instant.now(clock).isAfter(expiresAt);
        }
    }

    private record ParsedAnchor(String href, String text) {}

    private final UpcomingAlbumDataFetcher fetcher;

    private final Duration ttl;
    private final Clock clock;

    private CacheEntry cache;

    private final ObjectMapper mapper = new ObjectMapper();

    
    public MetalArchivesUpcomingAlbumProvider(
        UpcomingAlbumDataFetcher fetcher,
        Duration ttl,
        Clock clock) {
        this.fetcher = fetcher;
        this.ttl = ttl;
        this.clock = clock;
    }

    @Override
    public synchronized Album getRandomUpcomingAlbum() throws IOException, InterruptedException {
        if (cache == null || cache.isExpired(clock)) {
            cache = refresh();
        }

        return getRandomUpcomingAlbum(cache.data());
    }

    private CacheEntry refresh() throws IOException, InterruptedException{
        String doc = fetcher.fetch();
        JsonNode data = parseJSON(doc);
        Instant expiresAt = Instant.now(clock).plus(ttl);
        return new CacheEntry(data, expiresAt);
    }

    private Album getRandomUpcomingAlbum(JsonNode data) {
        JsonNode entry = data.get(ThreadLocalRandom.current().nextInt(data.size()));

        String bandHtml = entry.get(0).asText();
        String albumHtml = entry.get(1).asText();
        String type = entry.get(2).asText();
        String genre = entry.get(3).asText();

        ParsedAnchor band = parseAnchor(bandHtml);
        ParsedAnchor album = parseAnchor(albumHtml);

        return new Album(
            band.href(),
            band.text(),
            album.href(),
            album.text(),
            type,
            genre
        );
    }

    private JsonNode parseJSON(String json) throws JsonProcessingException {
        JsonNode root = mapper.readTree(json);
        JsonNode data = root.get("aaData");
        if (data == null || !data.isArray() || data.isEmpty()) {
            throw new IllegalStateException("No upcoming albums found");
        }
        return data;
    }

    private ParsedAnchor parseAnchor(String anchorHtml) {

        int anchorStart = anchorHtml.indexOf("<a");
        if (anchorStart == -1) {
            throw new IllegalArgumentException("Missing <a tag");
        }

        int tagEnd = anchorHtml.indexOf(">", anchorStart);
        if (tagEnd == -1) {
            throw new IllegalArgumentException("Malformed anchor tag");
        }

        int closeTagStart = anchorHtml.indexOf("</a>", tagEnd);
        if (closeTagStart == -1) {
            throw new IllegalArgumentException("Missing closing </a> tag");
        }

        int hrefStart = anchorHtml.indexOf("href=\"", anchorStart);
        if (hrefStart == -1) {
            throw new IllegalArgumentException("No href found");
        }

        int valueStart = hrefStart + 6;
        int valueEnd = anchorHtml.indexOf('"', valueStart);

        if (valueEnd == -1) {
            throw new IllegalArgumentException("Malformed href");
        }

        String href = anchorHtml.substring(valueStart, valueEnd);

    
        if (closeTagStart == tagEnd + 1) {
            throw new IllegalArgumentException("Empty anchor text");
        }

        String text = anchorHtml.substring(tagEnd + 1, closeTagStart);

        return new ParsedAnchor(href, text);
    }
}