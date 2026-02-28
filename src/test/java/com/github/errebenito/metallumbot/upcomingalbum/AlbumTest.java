package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlbumTest {
    @Test
    void shouldFormatAlbumCorrectly() {

        var album = new Album(
            "https://band.url",
            "Band Name",
            "https://album.url",
            "Album Name",
            "Full-length",
            "Metal"
        );

        String expected = """
            Band Name – Album Name
            https://album.url
            Full-length
            Metal""";

        assertEquals(expected, album.toString());
    }
}
