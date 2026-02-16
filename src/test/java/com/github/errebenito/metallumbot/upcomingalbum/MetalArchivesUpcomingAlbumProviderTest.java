package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

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
            new MetalArchivesUpcomingAlbumProvider("ignored", fakeFetcher);

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
            new MetalArchivesUpcomingAlbumProvider("ignored", fakeFetcher);

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            provider::getRandomUpcomingAlbum
        );

        assertEquals("No upcoming albums found", ex.getMessage());
    }
}
