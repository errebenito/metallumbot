package com.github.errebenito.metallumbot.randomband;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.errebenito.metallumbot.messaging.MessageSending;

public class MetalArchivesRandomBandUseCase implements RandomBandUseCase {
    private static final Logger LOGGER = LogManager.getLogger(MetalArchivesRandomBandUseCase.class);
    private final RandomBandProvider bandProvider;
    private final MessageSending messageSender;

    public MetalArchivesRandomBandUseCase(RandomBandProvider randomBandProvider, MessageSending messageSender) {
        this.bandProvider = randomBandProvider;
        this.messageSender = messageSender;
    }

    @Override
    public void sendBand(String receiver) {
        try {
            messageSender.sendMessage(receiver, bandProvider.getRandomBandUrl());
        } catch (InterruptedException e) {
            handleFailure(receiver, e);
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            handleFailure(receiver, ex);
        }
    }

    private void handleFailure(String receiver, Exception e) {
        LOGGER.error("Failed to retrieve random band: {}", e.getMessage());
        messageSender.sendMessage(receiver, "Failed to retrieve random band");
    }
}
