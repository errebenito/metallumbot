package com.github.errebenito.metallumbot.upcomingalbum;

import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.errebenito.metallumbot.helper.UpcomingAlbumHelper;
import com.github.errebenito.metallumbot.model.Album;

public class HtmlUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {
    private final String htmlUrl;
    HtmlFetcher fetcher;
    
    public HtmlUpcomingAlbumProvider(String htmlUrl, HtmlFetcher fetcher) {
        this.htmlUrl = htmlUrl;
        this.fetcher = fetcher;
    }

    @Override
    public Album getRandomUpcomingAlbum() throws Exception {
        Document doc = fetcher.fetch(htmlUrl);
        System.out.println(doc.toString());

        Elements rows = doc.select("#upcomingAlbumList tbody tr");
        System.out.println(rows.toString());
        if (rows.isEmpty()) {
            throw new IllegalStateException("No upcoming albums found");
        }

        Element row = rows.get(ThreadLocalRandom.current().nextInt(rows.size()));
        Elements cols = row.select("td");

        String bandHtml = cols.get(0).html();
        String albumHtml = cols.get(1).html();
        String type = cols.get(2).text(); 
        String genre = cols.get(3).text(); 

        return new Album( 
            UpcomingAlbumHelper.extractHref(bandHtml), 
            UpcomingAlbumHelper.extractText(bandHtml), 
            UpcomingAlbumHelper.extractHref(albumHtml), 
            UpcomingAlbumHelper.extractText(albumHtml), 
            type,
            genre
        );
    }
}

