package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RandomBandUseCaseTest {
    @Test
    void shouldReturnBandUrlWhenProviderSucceeds() {
        RandomBandProvider fakeProvider = () -> 
            "https://www.metal-archives.com/bands/Test/123";

        RandomBandUseCase useCase = new RandomBandUseCase(fakeProvider);

        String result = useCase.handle();

        assertEquals(
            "https://www.metal-archives.com/bands/Test/123",
            result
        );
    }

    @Test
    void shouldReturnErrorMessageWhenProviderFails() {
        RandomBandProvider failingProvider = () -> {
            throw new RuntimeException("Boom");
        };

        RandomBandUseCase useCase = new RandomBandUseCase(failingProvider);

        String result = useCase.handle();

        assertEquals("Failed to retrieve random band.", result);
    }
}
