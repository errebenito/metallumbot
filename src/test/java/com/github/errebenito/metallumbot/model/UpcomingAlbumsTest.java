package com.github.errebenito.metallumbot.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class UpcomingAlbumsTest {
    @Test
    void shouldReturnTrimmedLink() {
    UpcomingAlbums albums = new UpcomingAlbums();
    albums.setAlbumData(List.of(
        List.of("irrelevant", "<a href=\"https://www.metal-archives.com/albums/Foo\">Foo</a>")
    ));
    String result = albums.toString();
    assertEquals("https://www.metal-archives.com/albums/Foo", result);
    }
}
