package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.model.Album;

class MetalArchivesUpcomingAlbumProviderTest {

    @Test
    void shouldParseAlbumFromHtml() throws Exception {

        String html = """
            {
      "aaData": [
        [
          "<a href=\\"https://band\\">Test Band</a>",
          "<a href=\\"https://album\\">Test Album</a>",
          "Full-length",
          "Black Metal",
          "January 1st, 2026"
        ]
      ]
    }
        """;

        MetalArchivesDataFetcher fakeFetcher =
            url -> html;

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider("ignored", fakeFetcher, 
            Duration.of(1, ChronoUnit.SECONDS), Clock.systemUTC());

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Test Band", album.bandName());
        assertEquals("Test Album", album.albumName());
    }

    @Test
    void shouldThrowWhenNoRowsFound() {

        String html = """
        {
        "aaData": []
        }
        """;

        MetalArchivesDataFetcher fakeFetcher = url -> html;

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider("ignored", fakeFetcher, 
            Duration.of(1, ChronoUnit.SECONDS), Clock.systemUTC());

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            provider::getRandomUpcomingAlbum
        );

        assertEquals("No upcoming albums found", ex.getMessage());
    }

    @Test
    void shouldReuseCacheBeforeTtlExpires() throws Exception {
    
        String json = """
        {
        "aaData": [
            [
            "<a href=\\"https://band\\">Test Band</a>",
            "<a href=\\"https://album\\">Test Album</a>",
            "Full-length",
            "Black Metal"
            ]
        ]
        }
        """;

        AtomicInteger calls = new AtomicInteger();

        MetalArchivesDataFetcher fakeFetcher = url -> {
            calls.incrementAndGet();
            return json;
        };

        Instant baseTime = Instant.parse("2026-01-01T00:00:00Z");
        Clock clock = Clock.fixed(baseTime, ZoneOffset.UTC);

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                "ignored",
                fakeFetcher,
                Duration.ofHours(1),
                clock
            );

        provider.getRandomUpcomingAlbum();
        provider.getRandomUpcomingAlbum();

        assertEquals(1, calls.get());
    }

    @Test
    void shouldRefreshAfterTtlExpires() throws Exception {

        String json = """
        {
        "aaData": [
            [
            "<a href=\\"https://band\\">Test Band</a>",
            "<a href=\\"https://album\\">Test Album</a>",
            "Full-length",
            "Black Metal"
            ]
        ]
        }
        """;

        AtomicInteger calls = new AtomicInteger();

        MetalArchivesDataFetcher fakeFetcher = url -> {
            calls.incrementAndGet();
            return json;
        };

        Instant baseTime = Instant.parse("2026-01-01T00:00:00Z");
        MutableClock clock = new MutableClock(baseTime, ZoneOffset.UTC);

        MetalArchivesUpcomingAlbumProvider provider =
            new MetalArchivesUpcomingAlbumProvider(
                "ignored",
                fakeFetcher,
                Duration.ofHours(1),
                clock
            );

        provider.getRandomUpcomingAlbum();
        assertEquals(1, calls.get());

        clock.advance(Duration.ofHours(2));

        provider.getRandomUpcomingAlbum();
        assertEquals(2, calls.get());
    }
}
