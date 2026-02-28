package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetalArchivesRandomBandUseCaseTest {

    @Test
    @DisplayName("Verifies that the use case delegates to the message sender")
    void givenReceiverBandProviderAndMessageSenderWhenSendingBandThenShouldDelegateToSender() {

        var provider = new StubRandomBandProvider("https://band.url");
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertTrue(sender.called);
        assertEquals("https://band.url", sender.message);
    }

    @Test
    @DisplayName("Verifies that the response is sent to the correct destination")
    void givenReceiverBandProviderAndMessageSenderWhenSendingBandThenShouldSendToExpectedReceiver() {

        var provider = new StubRandomBandProvider("https://band.url");
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertEquals("42", sender.receiver);
    }

    @Test
    @DisplayName("Verifies that the response contains the expected content")
    void givenReceiverBandProviderAndMessageSenderWhenSendingBandThenShouldSendExpectedMessage() {

        var provider = new StubRandomBandProvider("https://band.url");
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertEquals("https://band.url", sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws any exception, the response contains an user-friendly error message")
    void givenExceptionIsThrownWhenSendingBandThenShouldSendErrorMessage() {

        var provider = new ThrowingRandomBandProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertEquals("Failed to retrieve random band", sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws an InterruptedException, the response contains an user-friendly error message")
    void givenInterruptedExceptionIsThrownWhenSendingBandThenShouldSendErrorMessage() {

        var provider = new InterruptingRandomBandProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendBand("42");

        assertEquals("Failed to retrieve random band", sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws an InterruptedException, the current thread is interrupted")
    void givenInterruptedExceptionIsThrownWhenSendingBandThenShouldInterruptCurrentThread() {

        var provider = new InterruptingRandomBandProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendBand("42");

        assertTrue(Thread.currentThread().isInterrupted());
    }
}
