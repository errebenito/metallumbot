package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MetalArchivesUpcomingAlbumProviderTest {

    private static String jsonWithBandAnchor(String bandAnchor) {
        String escaped = bandAnchor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");

        return """
        {
        "aaData": [
            [
            "%s",
            "<a href=\\"https://album\\">Test Album</a>",
            "Full-length",
            "Black Metal"
            ]
        ]
        }
        """.formatted(escaped);
    }

    private static String validNormalJson() {
        return jsonWithBandAnchor("<a href=\"https://band\">Test Band</a>");
    }

    private static Clock fixedClock(String isoInstant) {
        return Clock.fixed(Instant.parse(isoInstant), ZoneOffset.UTC);
    }

    @Test
    @DisplayName("Verifies that the provider delegates to the fetcher")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldDelegateToFetcher() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        provider.getRandomUpcomingAlbum();

        assertEquals(1, fetcher.calls);
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected band url")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedBandUrl() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("https://band", album.bandUrl());
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected band name")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedBandName() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Test Band", album.bandName());
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected album url")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedAlbumUrl() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("https://album", album.albumUrl());
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected album name")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedAlbumName() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Test Album", album.albumName());
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected type")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedType() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Full-length", album.type());
        assertEquals("Black Metal", album.genre());
    }

    @Test
    @DisplayName("Verifies that the provider returns data containing the expected genre")
    void givenProviderAndFetcherWhenReturningAlbumThenShouldReturnExpectedGenre() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Black Metal", album.genre());
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when there is no data to return")
    void givenFetcherReturnsNoDataWhenReturningAlbumThenShouldThrowException() {
        UpcomingAlbumDataFetcher fetcher = () -> "{}";

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

            assertThrows(IllegalStateException.class, provider::getRandomUpcomingAlbum);
    }

    @Test
    @DisplayName("Verifies that a user-friendly error message is returned when an exception occurs")
    void givenExceptionIsThrownWhenReturningAlbumThenShouldReturnErrorMessage() {
        UpcomingAlbumDataFetcher fetcher = () -> "{}";

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

        Exception ex = null;

        try {
            provider.getRandomUpcomingAlbum();
        } catch (Exception e) {
            ex = e;
        }

        assertEquals("No upcoming albums found", ex.getMessage());
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns an empty array")
    void givenFetcherReturnsEmptyArrayWhenReturningAlbumThenShouldThrowException() {
        UpcomingAlbumDataFetcher fetcher = () -> """
        { "aaData": [] }
        """;

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

        assertThrows(IllegalStateException.class, provider::getRandomUpcomingAlbum);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher does not return an array")
    void givenFetcherDoesNotReturnArrayWhenReturningAlbumThenShouldThrowException() {
        UpcomingAlbumDataFetcher fetcher = () -> """
            { "aaData": {} }
        """;

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

        assertThrows(IllegalStateException.class, provider::getRandomUpcomingAlbum);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data that is missing an anchor tag")
    void givenFetcherReturnsDataMissingAnchorTagWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<span href=\"x\">Text</span>");

        assertThrowsException(IllegalArgumentException.class, () -> json);        
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data that is missing an anchor tag has the expected message")
    void givenFetcherReturnsDataMissingAnchorTagWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<span href=\"x\">Text</span>");

        assertExceptionMessage("Missing <a tag", () -> json);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data that has a malformed opening tag")
    void givenFetcherReturnsDataWithMalformedOpeningTagsWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<a href=\"x\" Text");

        assertThrowsException(IllegalArgumentException.class, () -> json);
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data that has a malformed opening tag has the expected message")
    void givenFetcherReturnsDataWithMalformedOpeningTagsWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<a href=\"x\" Text");

        assertExceptionMessage("Malformed anchor tag", () -> json);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data that is missing a closing tag")
    void givenFetcherReturnsDataMissingClosingTagWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<a href=\"x\">Text");

        assertThrowsException(IllegalArgumentException.class, () -> json);
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data that is missing a closing tag has the expected message")
    void givenFetcherReturnsDataMissingClosingTagWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<a href=\"x\">Text");

        assertExceptionMessage("Missing closing </a> tag", () -> json);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data that is missing an href")
    void givenFetcherReturnsDataMissingHrefWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<a>Text</a>");

        assertThrowsException(IllegalArgumentException.class, () -> json);
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data that is missing an href has the expected message")
    void givenFetcherReturnsDataMissingHrefWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<a>Text</a>");

        assertExceptionMessage("No href found", () -> json);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data that has a malformed href")
    void givenFetcherReturnsDataWithMalformedHrefWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<a href=\"x>Text</a>");

        assertThrowsException(IllegalArgumentException.class, () -> json);
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data that has a malformed href has the expected message")
    void givenFetcherReturnsDataWithMalformedHrefWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<a href=\"x>Text</a>");

        assertExceptionMessage("Malformed href", () -> json);
    }

    @Test
    @DisplayName("Verifies that an exception is thrown when the fetcher returns data with empty text in an anchor")
    void givenFetcherReturnsDataWithEmptyTextInAnchorWhenReturningAlbumThenShouldThrowException() {
        String json = jsonWithBandAnchor("<a href=\"x\"></a>");

        assertThrowsException(IllegalArgumentException.class, () -> json);
    }

    @Test
    @DisplayName("Verifies that the exception when the fetcher returns data with empty text in an anchor has the expected message")
    void givenFetcherReturnsDataWithEmptyTextInAnchorWhenReturningAlbumThenExceptionShouldHaveExpectedErrorMessage() {
        String json = jsonWithBandAnchor("<a href=\"x\"></a>");

        assertExceptionMessage("Empty anchor text", () -> json);
    }


    private <T extends Throwable> T assertThrowsException(Class<T> type, UpcomingAlbumDataFetcher fetcher) {
        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(fetcher, Duration.ofHours(1), Clock.systemUTC());

        return assertThrows(type, provider::getRandomUpcomingAlbum);
    }

    private void assertExceptionMessage(String expectedMessage, UpcomingAlbumDataFetcher fetcher) {
        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(fetcher, Duration.ofHours(1), Clock.systemUTC());

        Exception ex = null;
        try {
            provider.getRandomUpcomingAlbum();
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex, "Expected an exception to be thrown");
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    @DisplayName("Verifies that the cache is used when it has not expired")
    void givenCacheIsNotExpiredWhenFetchingAlbumDataThenShouldUseCache() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        provider.getRandomUpcomingAlbum();
        provider.getRandomUpcomingAlbum();

        assertEquals(1, fetcher.calls);
    }

    @Test
    @DisplayName("Verifies that the cache is refreshed when it has expired")
    void givenCacheIsExpiredWhenFetchingAlbumDataThenShouldRefreshCache() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MutableClock clock = new MutableClock(
                Instant.parse("2024-01-01T00:00:00Z"),
                ZoneOffset.UTC
        );

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofSeconds(10),
                clock
            );

        provider.getRandomUpcomingAlbum();

        clock.advance(Duration.ofSeconds(11));

        provider.getRandomUpcomingAlbum();
        assertEquals(2, fetcher.calls);
    }
}