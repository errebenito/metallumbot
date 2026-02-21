package com.github.errebenito.metallumbot;

import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import com.github.errebenito.metallumbot.bot.CommandProcessor;
import com.github.errebenito.metallumbot.bot.MetallumBot;
import com.github.errebenito.metallumbot.bot.MetallumBotMessageSender;
import com.github.errebenito.metallumbot.helper.UpcomingAlbumHelper;
import com.github.errebenito.metallumbot.randomband.MetalArchivesRandomBandProvider;
import com.github.errebenito.metallumbot.randomband.RandomBandUseCase;
import com.github.errebenito.metallumbot.upcomingalbum.MetalArchivesUpcomingAlbumProvider;
import com.github.errebenito.metallumbot.upcomingalbum.UpcomingAlbumsFetcher;
import com.github.errebenito.metallumbot.upcomingalbum.RandomUpcomingAlbumUseCase;

public class Main {

    public static void main(String[] args) throws Exception {

        String botToken = System.getenv("METALLUM_BOT_TOKEN");

        var bandProvider = new MetalArchivesRandomBandProvider();
        var albumProvider = new MetalArchivesUpcomingAlbumProvider(
            UpcomingAlbumHelper.FULL_UPCOMING_ALBUMS_URL,
            new UpcomingAlbumsFetcher(),
            Duration.of(1, ChronoUnit.SECONDS),
        Clock.systemUTC());
        var bandHandler = new RandomBandUseCase(bandProvider);
        var albumHandler = new RandomUpcomingAlbumUseCase(albumProvider);
        var sender = new MetallumBotMessageSender(botToken);
        var commandProcessor = new CommandProcessor(bandHandler, albumHandler, sender);
        var bot = new MetallumBot(commandProcessor);

        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(botToken, bot);
            Thread.currentThread().join();
        }
    }
}
