package com.github.errebenito.metallumbot.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.CommandHandler;
import com.github.errebenito.metallumbot.model.Command;

class CommandProcessorTest {
    @Test
    @DisplayName("Verifies that the command to get a random band works")
    void givenBandCommandWhenProcessingCommandThenShouldInvokeBandUseCaseAndReturnBandLink() {
        CommandHandler fakeBandUseCase =
            () -> "https://www.metal-archives.com/bands/Test/123";

        CommandHandler fakeAlbumUseCase = 
            () -> "https://www.metal-archives.com/albums/Test/123";

        FakeTelegramSender fakeSender = new FakeTelegramSender();

        CommandProcessor processor =
            new CommandProcessor(fakeBandUseCase, fakeAlbumUseCase, fakeSender);
            

        processor.process(new Command("42", "/band"));

        assertEquals("42", fakeSender.lastChatId);
        assertEquals(
            "https://www.metal-archives.com/bands/Test/123",
            fakeSender.lastText
        );
    }

    @Test
    @DisplayName("Verifies that the command to get a random album works")
    void givenUpcomingAlbumCommandWhenProcessingCommandThenShouldInvokeUpcomingAlbumUseCaseAndReturnAlbumLink() {
        CommandHandler fakeBandUseCase =
            () -> "https://www.metal-archives.com/bands/Test/123";

        CommandHandler fakeAlbumUseCase = 
            () -> "https://www.metal-archives.com/albums/Test/123";    

        FakeTelegramSender fakeSender = new FakeTelegramSender();

        CommandProcessor processor =
            new CommandProcessor(fakeBandUseCase, fakeAlbumUseCase, fakeSender);
            

        processor.process(new Command("42", "/upcoming"));

        assertEquals("42", fakeSender.lastChatId);
        assertEquals(
            "https://www.metal-archives.com/albums/Test/123",
            fakeSender.lastText
        );
    }

    @Test
    @DisplayName("Verifies that any other command returns the usage instructions")
    void givenOtherCommandWhenProcessingCommandThenShouldShowUsageInstructions() {
        CommandHandler fakeUseCase = () -> "ignored";
        FakeTelegramSender fakeSender = new FakeTelegramSender();

        CommandProcessor processor =
            new CommandProcessor(fakeUseCase, fakeUseCase, fakeSender);

        processor.process(new Command("42", "/start"));

        assertTrue(fakeSender.lastText.contains("Usage"));
    }

    @Test
    @DisplayName("Verifies that a message is logged if sending the response to a command fails")
    void givenSendingResponseToCommandFailsWhenProcessingCommandThenShouldLogMessage() {
        CommandHandler fakeBandUseCase =
            () -> "https://www.metal-archives.com/bands/Test/123";

        CommandHandler fakeAlbumUseCase = 
            () -> "https://www.metal-archives.com/albums/Test/123";

        FakeTelegramSender fakeSender = new FakeTelegramSender(true);


        CommandProcessor processor =
            new CommandProcessor(fakeBandUseCase, fakeAlbumUseCase, fakeSender);

        Logger logger = LogManager.getLogger(CommandProcessor.class);
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        LoggerConfig loggerConfig = context.getConfiguration().getLoggerConfig(logger.getName());

        ListAppender listAppender = ListAppender.create("TestAppender");
        listAppender.start();
        loggerConfig.addAppender(listAppender, null, null);
        context.updateLoggers();

        processor.process(new Command("42", "/band"));

        assertEquals(1, listAppender.getEvents().size());

        String loggedMessage = listAppender.getEvents().get(0).getMessage().getFormattedMessage();
        assertEquals("simulated failure", loggedMessage);

        loggerConfig.removeAppender("TestAppender");
        listAppender.stop();
    }
}
