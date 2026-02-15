package com.github.errebenito.metallumbot.upcomingalbum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.errebenito.metallumbot.CommandHandler;
import com.github.errebenito.metallumbot.model.Album;

public class RandomUpcomingAlbumUseCase implements CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(RandomUpcomingAlbumUseCase.class);

    private final RandomUpcomingAlbumProvider provider;

    public RandomUpcomingAlbumUseCase(RandomUpcomingAlbumProvider provider) {
        this.provider = provider;
    }

    @Override
    public String handle() {
        try {
            Album album = provider.getRandomUpcomingAlbum();
            return String.format("%s – %s%n%s%n%s%n%s", album.bandName(), album.albumName(), album.albumUrl(), album.type(),album.genre());
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve upcoming album: {}", e.getMessage());
            return "Failed to retrieve upcoming album.";
        }
    }

}
