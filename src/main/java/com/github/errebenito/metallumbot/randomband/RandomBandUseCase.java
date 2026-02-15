package com.github.errebenito.metallumbot.randomband;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.errebenito.metallumbot.CommandHandler;

public class RandomBandUseCase implements CommandHandler {
    private static final Logger LOGGER = LogManager.getLogger(RandomBandUseCase.class);
    private final RandomBandProvider bandProvider;

    public RandomBandUseCase(RandomBandProvider randomBandProvider) {
        this.bandProvider = randomBandProvider;
    }

    public String handle() {
        try {
            return bandProvider.getRandomBandUrl();
        } catch (Exception e) {
            LOGGER.error("Failed to retrieve random band: {}", e.getMessage());
            return "Failed to retrieve random band.";
        }
    }
}
