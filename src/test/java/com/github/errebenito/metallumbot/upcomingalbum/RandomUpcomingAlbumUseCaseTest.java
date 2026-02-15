package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.model.Album;

class RandomUpcomingAlbumUseCaseTest {

    @Test
    void shouldFormatAlbumCorrectly() {

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
    void shouldReturnErrorMessageWhenProviderFails() {

        RandomUpcomingAlbumProvider failingProvider =
            () -> { throw new RuntimeException("boom"); };

        RandomUpcomingAlbumUseCase useCase =
            new RandomUpcomingAlbumUseCase(failingProvider);

        String result = useCase.handle();

        assertEquals("Failed to retrieve upcoming album.", result);
    }
}

