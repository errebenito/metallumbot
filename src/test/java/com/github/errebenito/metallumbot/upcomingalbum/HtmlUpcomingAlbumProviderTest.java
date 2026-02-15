package com.github.errebenito.metallumbot.upcomingalbum;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import com.github.errebenito.metallumbot.model.Album;

class HtmlUpcomingAlbumProviderTest {

    @Test
    void shouldParseAlbumFromHtml() throws Exception {

        String html = """
            <table id="upcomingAlbumList">
                <tbody>
                    <tr>
                        <td><a href="https://band">Test Band</a></td>
                        <td><a href="https://album">Test Album</a></td>
                        <td>Full-length</td>
                        <td>Black Metal</td>
                        <td>January 1st, 2026</td>
                    </tr>
                </tbody>
            </table>
        """;

        HtmlFetcher fakeFetcher =
            url -> Jsoup.parse(html);

        HtmlUpcomingAlbumProvider provider =
            new HtmlUpcomingAlbumProvider("ignored", fakeFetcher);

        Album album = provider.getRandomUpcomingAlbum();

        assertEquals("Test Band", album.bandName());
        assertEquals("Test Album", album.albumName());
    }

    @Test
    void shouldThrowWhenNoRowsFound() {

        String html = """
            <table id="upcomingAlbumList">
                <tbody>
                </tbody>
            </table>
        """;

        HtmlFetcher fakeFetcher = url -> Jsoup.parse(html);

        HtmlUpcomingAlbumProvider provider =
            new HtmlUpcomingAlbumProvider("ignored", fakeFetcher);

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            provider::getRandomUpcomingAlbum
        );

        assertEquals("No upcoming albums found", ex.getMessage());
    }
}
