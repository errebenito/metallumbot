package com.github.errebenito.metallumbot.bot;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.github.errebenito.metallumbot.model.Command;

public class MetallumBot implements LongPollingSingleThreadUpdateConsumer {
  
  private final CommandProcessor processor;

    public MetallumBot(CommandProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void consume(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatId =
                update.getMessage().getChatId().toString();

            String text =
                update.getMessage().getText();

            processor.process(new Command(chatId, text));
        }
    }
}
