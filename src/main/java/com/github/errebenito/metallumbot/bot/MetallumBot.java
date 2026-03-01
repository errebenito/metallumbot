package com.github.errebenito.metallumbot.bot;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.github.errebenito.metallumbot.randomband.RandomBandUseCase;
import com.github.errebenito.metallumbot.unknowncommand.UnknownCommandUseCase;
import com.github.errebenito.metallumbot.upcomingalbum.RandomUpcomingAlbumUseCase;

public class MetallumBot implements LongPollingSingleThreadUpdateConsumer {
    
    private final UnknownCommandUseCase unknownCommandUseCase;
    private final RandomBandUseCase randomBandUseCase;
    private final RandomUpcomingAlbumUseCase randomUpcomingAlbumUseCase;

    public MetallumBot(
        UnknownCommandUseCase unknownCommandUseCase,
        RandomBandUseCase randomBandUseCase,
        RandomUpcomingAlbumUseCase randomUpcomingAlbumUseCase
    ) {
        this.unknownCommandUseCase = unknownCommandUseCase;
        this.randomBandUseCase = randomBandUseCase;
        this.randomUpcomingAlbumUseCase = randomUpcomingAlbumUseCase;
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String receiver = update.getMessage().getChatId().toString();

            String command = update.getMessage().getText();
            
            if ("/band".equals(command)) {
                randomBandUseCase.sendBand(receiver);
            } else if ("/upcoming".equals(command)) {
                randomUpcomingAlbumUseCase.sendUpcomingAlbum(receiver);
            } else {
                unknownCommandUseCase.sendInstructions(receiver);
            }
        }
    }
}