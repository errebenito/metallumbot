package com.github.errebenito.metallumbot.unknowncommand;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetallumBotUnknownCommandUseCaseTest {

    @Test
    @DisplayName("Verifies that the use case delegates to the message sender")
    void givenReceiverAndMessageSenderWhenReplyingToUnknownCommandThenShouldDelegateToSender() {

        var sender = new RecordingMessageSender();
        var useCase = new MetallumBotUnknownCommandUseCase(sender);

        useCase.sendInstructions("42");

        assertTrue(sender.called);
    }

    @Test
    @DisplayName("Verifies that the use case sends replies to the correct receiver")
    void givenReceiverAndMessageSenderWhenReplyingToUnknownCommandThenShouldSendResponseToExpectedReceiver() {

        var sender = new RecordingMessageSender();
        var useCase = new MetallumBotUnknownCommandUseCase(sender);

        useCase.sendInstructions("42");

        assertEquals("42", sender.receiver);
    }

    @Test
    @DisplayName("Verifies that the use case replies to unknown commands by sending usage instructions")
    void givenReceiverAndMessageSenderWhenReplyingToUnknownCommandThenShouldSendExpectedMessage() {

        var sender = new RecordingMessageSender();
        var useCase = new MetallumBotUnknownCommandUseCase(sender);

        useCase.sendInstructions("42");

        assertEquals(MetallumBotUnknownCommandUseCase.USAGE,
            sender.message);
    }
}
