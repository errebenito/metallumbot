package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
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
    void shouldReturnParsedAlbum() throws Exception {
        CountingFetcher fetcher = new CountingFetcher(validNormalJson());

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                fixedClock("2024-01-01T00:00:00Z")
            );

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("https://band", album.bandUrl());
        assertEquals("Test Band", album.bandName());
        assertEquals("https://album", album.albumUrl());
        assertEquals("Test Album", album.albumName());
        assertEquals("Full-length", album.type());
        assertEquals("Black Metal", album.genre());
        assertEquals(1, fetcher.calls);
    }

    @Test
    void shouldThrowWhenNoAaData() {
        UpcomingAlbumDataFetcher fetcher = () -> "{}";

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

        IllegalStateException ex =
            assertThrows(IllegalStateException.class, provider::getRandomUpcomingAlbum);

        assertEquals("No upcoming albums found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenAaDataEmpty() {
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
    void shouldThrowWhenAaDataIsNotArray() {
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
    void shouldThrowWhenMissingATag() {
        String json = jsonWithBandAnchor("<span href=\"x\">Text</span>");

        assertAnchorFailure(json, "Missing <a tag");
    }

    @Test
    void shouldThrowWhenMalformedOpeningTag() {
        String json = jsonWithBandAnchor("<a href=\"x\" Text");

        assertAnchorFailure(json, "Malformed anchor tag");
    }

    @Test
    void shouldThrowWhenMissingClosingTag() {
        String json = jsonWithBandAnchor("<a href=\"x\">Text");

        assertAnchorFailure(json, "Missing closing </a> tag");
    }

    @Test
    void shouldThrowWhenNoHrefFound() {
        String json = jsonWithBandAnchor("<a>Text</a>");

        assertAnchorFailure(json, "No href found");
    }

    @Test
    void shouldThrowWhenMalformedHref() {
        String json = jsonWithBandAnchor("<a href=\"x>Text</a>");

        assertAnchorFailure(json, "Malformed href");
    }

    @Test
    void shouldThrowWhenEmptyText() {
        String json = jsonWithBandAnchor("<a href=\"x\"></a>");

        assertAnchorFailure(json, "Empty anchor text");
    }

    private void assertAnchorFailure(String json, String expectedMessage) {
        UpcomingAlbumDataFetcher fetcher = () -> json;

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                fetcher,
                Duration.ofHours(1),
                Clock.systemUTC()
            );

        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, provider::getRandomUpcomingAlbum);

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldUseCacheWhenNotExpired() throws Exception {
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
    void shouldRefreshWhenExpired() throws Exception {
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
        assertEquals(1, fetcher.calls);

        clock.advance(Duration.ofSeconds(11));

        provider.getRandomUpcomingAlbum();
        assertEquals(2, fetcher.calls);
    }
}