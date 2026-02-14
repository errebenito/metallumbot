package com.github.errebenito.metallumbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import com.github.errebenito.metallumbot.bot.CommandProcessor;
import com.github.errebenito.metallumbot.bot.MetallumBot;
import com.github.errebenito.metallumbot.bot.MetallumBotMessageSender;
import com.github.errebenito.metallumbot.randomband.MetalArchivesRandomBandProvider;
import com.github.errebenito.metallumbot.randomband.RandomBandUseCase;

public class Main {
    public static void main(String[] args) throws Exception {

        String botToken = System.getenv("METALLUM_BOT_TOKEN");

        var bandProvider = new MetalArchivesRandomBandProvider();
        var bandHandler = new RandomBandUseCase(bandProvider);
        var sender = new MetallumBotMessageSender(botToken);
        var commandProcessor = new CommandProcessor(bandHandler, sender);
        var bot = new MetallumBot(commandProcessor);

        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(botToken, bot);
            Thread.currentThread().join();
        }
    }
}
