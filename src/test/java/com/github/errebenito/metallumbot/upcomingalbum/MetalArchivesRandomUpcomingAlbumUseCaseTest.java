package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetalArchivesRandomUpcomingAlbumUseCaseTest {
    @Test
    void shouldSendFormattedAlbum() {

        var album = new Album(
            "bandUrl",
            "Band Name",
            "albumUrl",
            "Album Name",
            "Full-length",
            "Metal"
        );

        var provider = new StubUpcomingAlbumProvider(album);
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        useCase.sendUpcomingAlbum("42");

        assertTrue(sender.called);
        assertEquals("42", sender.receiver);
        assertEquals(album.toString(), sender.message);
    }

    @Test
    void shouldSendFallbackMessageWhenProviderFails() {

        var provider = new ThrowingUpcomingAlbumProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        useCase.sendUpcomingAlbum("42");

        assertTrue(sender.called);
        assertEquals("Failed to retrieve upcoming album", sender.message);
    }

    @Test
    void shouldReinterruptThreadWhenInterrupted() {

        var provider = new InterruptingUpcomingAlbumProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendUpcomingAlbum("42");

        assertTrue(sender.called);
        assertEquals("Failed to retrieve upcoming album", sender.message);
        assertTrue(Thread.currentThread().isInterrupted());
    }
}
