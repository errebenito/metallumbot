package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.errebenito.metallumbot.model.Album;

class CurlUpcomingAlbumProviderTest {

  @Test
  void shouldParseRandomAlbumFromJson() throws Exception {
    String json = """
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

    Executor fakeExecutor = url -> json;
    
    CurlUpcomingAlbumProvider provider =
      new CurlUpcomingAlbumProvider("ignored", fakeExecutor);

    Album album = provider.getRandomUpcomingAlbum();

    assertEquals("Test Band", album.bandName());
    assertEquals("Test Album", album.albumName());
    assertEquals("https://album", album.albumUrl());
  }

  @ParameterizedTest
  @MethodSource("jsonResults")
  void shouldThrowWhenAaDataIsMissing(String json) {

    Executor fakeExecutor = url -> json;

    CurlUpcomingAlbumProvider provider =
      new CurlUpcomingAlbumProvider("ignored", fakeExecutor);

    IllegalStateException ex = assertThrows(
      IllegalStateException.class,
      provider::getRandomUpcomingAlbum
    );

    assertEquals("No upcoming albums found", ex.getMessage());
  }

  public static Stream<String> jsonResults() {
    return Stream.of(
      """
      {
        "aaData": "not-an-array"
      }
      """
      ,
      """
      {
        "somethingElse": []
      }
      """
      ,
      """
      {
        "aaData": []
      }
      """
    );
  }
}

