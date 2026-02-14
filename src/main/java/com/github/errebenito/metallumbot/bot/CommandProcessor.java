package com.github.errebenito.metallumbot.bot;

import com.github.errebenito.metallumbot.model.Command;
import com.github.errebenito.metallumbot.randomband.CommandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandProcessor {
        private static final Logger LOGGER = LogManager.getLogger(CommandProcessor.class);

    private final CommandHandler randomBandUseCase;
    private final TelegramSender sender;

    public CommandProcessor(CommandHandler randomBandUseCase, TelegramSender sender) {
        this.randomBandUseCase = randomBandUseCase;
        this.sender = sender;
    }

    public void process(Command command) {

        if ("/band".equals(command.text())) {

            String result = randomBandUseCase.handle();

            try {
                sender.sendMessage(command.chatId(), result);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
