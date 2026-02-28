package com.github.errebenito.metallumbot.unknowncommand;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetallumBotUnknownCommandUseCaseTest {

    @Test
    void shouldSendUsageInstructions() {

        var sender = new RecordingMessageSender();
        var useCase = new MetallumBotUnknownCommandUseCase(sender);

        useCase.sendInstructions("42");

        assertTrue(sender.called);
        assertEquals("42", sender.receiver);
        assertEquals(MetallumBotUnknownCommandUseCase.USAGE,
            sender.message);
    }
}
