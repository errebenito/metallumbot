package com.github.errebenito.metallumbot;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import com.github.errebenito.metallumbot.bot.CommandProcessor;
import com.github.errebenito.metallumbot.bot.MetallumBot;
import com.github.errebenito.metallumbot.bot.MetallumBotMessageSender;
import com.github.errebenito.metallumbot.helper.UpcomingAlbumHelper;
import com.github.errebenito.metallumbot.randomband.MetalArchivesRandomBandProvider;
import com.github.errebenito.metallumbot.randomband.RandomBandUseCase;
import com.github.errebenito.metallumbot.upcomingalbum.CurlProcessStarter;
import com.github.errebenito.metallumbot.upcomingalbum.CurlUpcomingAlbumProvider;
import com.github.errebenito.metallumbot.upcomingalbum.HtmlUpcomingAlbumProvider;
import com.github.errebenito.metallumbot.upcomingalbum.JsoupHtmlFetcher;
import com.github.errebenito.metallumbot.upcomingalbum.RandomUpcomingAlbumProvider;
import com.github.errebenito.metallumbot.upcomingalbum.RandomUpcomingAlbumUseCase;
import com.github.errebenito.metallumbot.upcomingalbum.SystemCurlExecutor;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        String botToken = System.getenv("METALLUM_BOT_TOKEN");

        var bandProvider = new MetalArchivesRandomBandProvider();
        var albumProvider = getAlbumProviderInstance();
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

    private static RandomUpcomingAlbumProvider getAlbumProviderInstance() {
        if (isCurlAvailable()) {
            return new CurlUpcomingAlbumProvider(UpcomingAlbumHelper.FULL_UPCOMING_ALBUMS_URL, new SystemCurlExecutor(new CurlProcessStarter()));
        }
        return new HtmlUpcomingAlbumProvider(UpcomingAlbumHelper.PARTIAL_UPCOMING_ALBUMS_URL, new JsoupHtmlFetcher());
    }

    private static boolean isCurlAvailable() {
        try {
            Process process = new ProcessBuilder("curl", "--version")
                .redirectErrorStream(true) 
                .start();

            if (!process.waitFor(500, TimeUnit.MILLISECONDS)) {
                process.destroy();
                return false;
            }

            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Failed to run curl: {}", e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
