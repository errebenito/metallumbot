package com.github.errebenito.metallumbot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import com.sun.net.httpserver.HttpServer;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import com.github.errebenito.metallumbot.bot.MetallumBot;
import com.github.errebenito.metallumbot.messaging.TelegramMessageSender;
import com.github.errebenito.metallumbot.randomband.MetalArchivesRandomBandProvider;
import com.github.errebenito.metallumbot.randomband.MetalArchivesRandomBandUseCase;
import com.github.errebenito.metallumbot.unknowncommand.MetallumBotUnknownCommandUseCase;
import com.github.errebenito.metallumbot.upcomingalbum.MetalArchivesUpcomingAlbumProvider;
import com.github.errebenito.metallumbot.upcomingalbum.MetalArchivesUpcomingAlbumsDataFetcher;
import com.github.errebenito.metallumbot.upcomingalbum.MetalArchivesRandomUpcomingAlbumUseCase;

public class Main {

    public static void main(String[] args) throws Exception {

        final String FULL_UPCOMING_ALBUMS_URL = "https://www.metal-archives.com/release/ajax-upcoming/json/1";

        String botToken = System.getenv("METALLUM_BOT_TOKEN");

        var bandProvider = new MetalArchivesRandomBandProvider();
        var albumProvider = new MetalArchivesUpcomingAlbumProvider(
            new MetalArchivesUpcomingAlbumsDataFetcher(FULL_UPCOMING_ALBUMS_URL),
            Duration.of(12, ChronoUnit.HOURS),
        Clock.systemUTC());
        var sender = new TelegramMessageSender(botToken);

        var unknownCommandUseCase = new MetallumBotUnknownCommandUseCase(sender);
        var bandUseCase = new MetalArchivesRandomBandUseCase(bandProvider, sender);
        var albumUseCase = new MetalArchivesRandomUpcomingAlbumUseCase(albumProvider, sender);
        var bot = new MetallumBot(unknownCommandUseCase, bandUseCase, albumUseCase);

        startHttpServer();

        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(botToken, bot);
            Thread.currentThread().join();
        }
    }

    private static void startHttpServer() throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "10000"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        server.setExecutor(null);
        server.start();
    }
}
