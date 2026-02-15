package com.github.errebenito.metallumbot.bot;

import com.github.errebenito.metallumbot.CommandHandler;
import com.github.errebenito.metallumbot.model.Command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandProcessor {
    private static final Logger LOGGER = LogManager.getLogger(CommandProcessor.class);

    private static final String USAGE = """
        Usage:
        /band Returns a random band
        /upcoming Returns a random upcoming release""";

    private final CommandHandler randomBandUseCase;
    private final CommandHandler randomUpcomingAlbumUseCase;
    private final TelegramSender sender;

    public CommandProcessor(CommandHandler randomBandUseCase, CommandHandler randomUpcomingAlbumUseCase, TelegramSender sender) {
        this.randomBandUseCase = randomBandUseCase;
        this.randomUpcomingAlbumUseCase = randomUpcomingAlbumUseCase;
        this.sender = sender;
    }

    public void process(Command command) {

        String result = USAGE;
        
        if ("/band".equals(command.text())) {
            result = randomBandUseCase.handle();
        }

        if ("/upcoming".equals(command.text())) {
            result = randomUpcomingAlbumUseCase.handle();
        }

        try {
                sender.sendMessage(command.chatId(), result);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
    }
}
