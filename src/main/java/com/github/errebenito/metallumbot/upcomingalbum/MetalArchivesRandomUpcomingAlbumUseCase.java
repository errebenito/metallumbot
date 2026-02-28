package com.github.errebenito.metallumbot.upcomingalbum;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.errebenito.metallumbot.messaging.MessageSending;

public class MetalArchivesRandomUpcomingAlbumUseCase implements RandomUpcomingAlbumUseCase {
    private static final Logger LOGGER = LogManager.getLogger(MetalArchivesRandomUpcomingAlbumUseCase.class);

    private final RandomUpcomingAlbumProvider albumProvider;
    private final MessageSending messageSender;


    public MetalArchivesRandomUpcomingAlbumUseCase(RandomUpcomingAlbumProvider albumProvider, MessageSending messageSender) {
        this.albumProvider = albumProvider;
        this.messageSender = messageSender;
    }

    @Override
    public void sendUpcomingAlbum(String receiver) {
        try {
            messageSender.sendMessage(receiver, albumProvider.getRandomUpcomingAlbum().toString());
        } catch (InterruptedException e) {
            handleFailure(receiver, e);
            Thread.currentThread().interrupt(); 
        } catch (Exception ex) {
            handleFailure(receiver, ex);
        }
    }

    private void handleFailure(String receiver, Exception e) {
        LOGGER.error("Failed to retrieve upcoming album: {}", e.getMessage());
        messageSender.sendMessage(receiver, "Failed to retrieve upcoming album");
    }
}
