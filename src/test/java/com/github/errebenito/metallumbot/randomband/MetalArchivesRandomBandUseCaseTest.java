package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetalArchivesRandomBandUseCaseTest {

    @Test
    void shouldSendRandomBandUrl() {

        var provider = new StubRandomBandProvider("https://band.url");
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertTrue(sender.called);
        assertEquals("42", sender.receiver);
        assertEquals("https://band.url", sender.message);
    }

    @Test
    void shouldSendFallbackMessageWhenProviderFails() {

        var provider = new ThrowingRandomBandProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        useCase.sendBand("42");

        assertTrue(sender.called);
        assertEquals("42", sender.receiver);
        assertEquals("Failed to retrieve random band", sender.message);
    }

    @Test
    void shouldReinterruptThreadWhenInterrupted() {

        var provider = new InterruptingRandomBandProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomBandUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendBand("42");

        assertTrue(sender.called);
        assertEquals("Failed to retrieve random band", sender.message);
        assertTrue(Thread.currentThread().isInterrupted());
    }
}
