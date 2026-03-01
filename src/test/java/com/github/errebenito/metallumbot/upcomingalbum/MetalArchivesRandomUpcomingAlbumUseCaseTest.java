package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.shared.RecordingMessageSender;

class MetalArchivesRandomUpcomingAlbumUseCaseTest {
    @Test
    @DisplayName("Verifies that the use case delegates to the sender")
    void givenReceiverAlbumProviderMessageSenderAndAlbumWhenSendingAlbumThenShouldDelegateToSender() {

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
    }

    @Test
    @DisplayName("Verifies that the use case sends the album to the expected destination")
    void givenReceiverAlbumProviderMessageSenderAndAlbumWhenSendingAlbumThenShouldSendToExpectedReceiver() {

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

        assertEquals("42", sender.receiver);
    }

    @Test
    @DisplayName("Verifies that the use case sends the expected message")
    void givenReceiverAlbumProviderMessageSenderAndAlbumWhenSendingAlbumThenShouldSendExpectedMessage() {

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

        assertEquals(album.toString(), sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws any exception, the response contains an user-friendly error message")
    void givenExceptionIsThrownWhenSendingAlbumThenShouldSendErrorMessage() {

        var provider = new ThrowingUpcomingAlbumProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        useCase.sendUpcomingAlbum("42");

        assertEquals("Failed to retrieve upcoming album", sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws an InterruptedException, the response contains an user-friendly error message")
    void givenInterruptedExceptionIsThrownWhenSendingAlbumThenShouldSendErrorMessage() {

        var provider = new InterruptingUpcomingAlbumProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendUpcomingAlbum("42");

        assertEquals("Failed to retrieve upcoming album", sender.message);
    }

    @Test
    @DisplayName("Verifies that when the provider throws an InterruptedException, the current thread is interrupted")
    void givenInterruptedExceptionIsThrownWhenSendingAlbumThenShouldInterruptCurrentThread() {
        var provider = new InterruptingUpcomingAlbumProvider();
        var sender = new RecordingMessageSender();

        var useCase = new MetalArchivesRandomUpcomingAlbumUseCase(provider, sender);

        Thread.interrupted();

        useCase.sendUpcomingAlbum("42");

        assertTrue(Thread.currentThread().isInterrupted());
    }
}
