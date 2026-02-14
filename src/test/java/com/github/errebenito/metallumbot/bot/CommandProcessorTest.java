package com.github.errebenito.metallumbot.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.model.Command;
import com.github.errebenito.metallumbot.randomband.CommandHandler;

class CommandProcessorTest {
    @Test
    void shouldSendBandUrlForBandCommand() {

        CommandHandler fakeUseCase =
            () -> "https://www.metal-archives.com/bands/Test/123";

        FakeTelegramSender fakeSender = new FakeTelegramSender();

        CommandProcessor processor =
            new CommandProcessor(fakeUseCase, fakeSender);

        processor.process(new Command("42", "/band"));

        assertEquals("42", fakeSender.lastChatId);
        assertEquals(
            "https://www.metal-archives.com/bands/Test/123",
            fakeSender.lastText
        );
    }

    @Test
    void shouldIgnoreOtherCommands() {

        CommandHandler fakeUseCase = () -> "ignored";
        FakeTelegramSender fakeSender = new FakeTelegramSender();

        CommandProcessor processor =
            new CommandProcessor(fakeUseCase, fakeSender);

        processor.process(new Command("42", "/start"));

        assertNull(fakeSender.lastText);
    }

    @Test
    void shouldLogWhenSendMessageThrows() {

        CommandHandler fakeUseCase =
            () -> "https://www.metal-archives.com/bands/Test/123";

        FakeTelegramSender fakeSender = new FakeTelegramSender(true);


        CommandProcessor processor =
            new CommandProcessor(fakeUseCase, fakeSender);

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
