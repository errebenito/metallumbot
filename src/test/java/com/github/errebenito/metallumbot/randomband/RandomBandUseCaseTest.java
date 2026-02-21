package com.github.errebenito.metallumbot.randomband;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomBandUseCaseTest {
    @Test
    @DisplayName("Verifies that a band URL is returned")
    void givenBandProviderSuccessWhenHandlingBandUseCaseThenShouldReturnBandLink() {
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
    @DisplayName("Verifies that an error message is returned if it was not possible to obtain a random band")
    void givenBandProviderFailureWhenHandlingBandUseCaseThenShouldReturnErrorMessage() {
        RandomBandProvider failingProvider = () -> {
            throw new RuntimeException("Boom");
        };

        RandomBandUseCase useCase = new RandomBandUseCase(failingProvider);

        String result = useCase.handle();

        assertEquals("Failed to retrieve random band.", result);
    }
}
