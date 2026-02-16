package com.github.errebenito.metallumbot.upcomingalbum;

import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.errebenito.metallumbot.helper.UpcomingAlbumHelper;
import com.github.errebenito.metallumbot.model.Album;

public class MetalArchivesUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {
    private final String jsonUrl;
    MetalArchivesDataFetcher fetcher;
    
    public MetalArchivesUpcomingAlbumProvider(String jsonUrl, MetalArchivesDataFetcher fetcher) {
        this.jsonUrl = jsonUrl;
        this.fetcher = fetcher;
    }

    @Override
    public Album getRandomUpcomingAlbum() throws Exception {
        String doc = fetcher.fetch(jsonUrl);
        JsonNode data = parseJSON(doc);
        return getRandomUpcomingAlbum(data);
    }

    private Album getRandomUpcomingAlbum(JsonNode data) {
        JsonNode entry = data.get(ThreadLocalRandom.current().nextInt(data.size()));

        String bandHtml = entry.get(0).asText();
        String albumHtml = entry.get(1).asText();
        String type = entry.get(2).asText();
        String genre = entry.get(3).asText();
        
        return new Album(
            UpcomingAlbumHelper.extractHref(bandHtml),
            UpcomingAlbumHelper.extractText(bandHtml),
            UpcomingAlbumHelper.extractHref(albumHtml),
            UpcomingAlbumHelper.extractText(albumHtml), 
            type,
            genre
        );
    }

    private JsonNode parseJSON(String json) throws JsonProcessingException {
        JsonNode root = new ObjectMapper().readTree(json);
        JsonNode data = root.get("aaData");
        if (data == null || !data.isArray() || data.isEmpty()) {
            throw new IllegalStateException("No upcoming albums found");
        }
        return data;
    }
}