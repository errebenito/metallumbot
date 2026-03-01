package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AlbumTest {
    @Test
    @DisplayName("Verifies that the string representation of an album matches the expected format")
    void givenAlbumWhenConvertingToStringRepresentationThenShouldEqualExpectedValue() {

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
