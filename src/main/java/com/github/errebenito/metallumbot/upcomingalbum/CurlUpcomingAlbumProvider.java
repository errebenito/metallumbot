package com.github.errebenito.metallumbot.upcomingalbum;

import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.errebenito.metallumbot.helper.UpcomingAlbumHelper;
import com.github.errebenito.metallumbot.model.Album;

public class CurlUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {
    private final String jsonUrl;
    private final Executor curlExecutor;

    public CurlUpcomingAlbumProvider(String jsonUrl, Executor curlExecutor) {
        this.jsonUrl = jsonUrl;
        this.curlExecutor = curlExecutor;
    }

    @Override
    public Album getRandomUpcomingAlbum() throws Exception {
        String json = curlExecutor.execute(jsonUrl);
        JsonNode data = parseJSON(json);
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