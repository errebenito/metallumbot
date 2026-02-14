package com.github.errebenito.metallumbot.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
