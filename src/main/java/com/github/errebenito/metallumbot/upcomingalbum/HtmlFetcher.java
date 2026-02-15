package com.github.errebenito.metallumbot.upcomingalbum;

import org.jsoup.nodes.Document;

public interface HtmlFetcher {
    Document fetch(String url) throws Exception;
}
