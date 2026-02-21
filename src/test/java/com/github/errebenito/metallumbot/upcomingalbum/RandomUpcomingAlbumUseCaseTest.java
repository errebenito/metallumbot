package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.model.Album;

class RandomUpcomingAlbumUseCaseTest {

    @Test
    @DisplayName("Verifies that the returned data for upcoming album commands is correctly formatted")
    void givenUpcomingAlbumRequestWhenHandlingItThenShouldCorrectlyFormatAlbumData() {
        RandomUpcomingAlbumProvider fakeProvider = () ->
            new Album(
                "https://band",
                "Test Band",
                "https://album",
                "Test Album",
                "Demo",
                "Metal"
            );

        RandomUpcomingAlbumUseCase useCase =
            new RandomUpcomingAlbumUseCase(fakeProvider);

        String result = useCase.handle();

        assertEquals(
            "Test Band – Test Album\nhttps://album\nDemo\nMetal",
            result
        );
    }

    @Test
    @DisplayName("Verifies that an error message is returned if it was not possible to obtain an upcoming album")
    void givenUpcomingAlbumRequestWhenHandlingFailsThenShouldReturnErrorMessage() {
        RandomUpcomingAlbumProvider failingProvider =
            () -> { throw new RuntimeException("boom"); };

        RandomUpcomingAlbumUseCase useCase =
            new RandomUpcomingAlbumUseCase(failingProvider);

        String result = useCase.handle();

        assertEquals("Failed to retrieve upcoming album.", result);
    }
}