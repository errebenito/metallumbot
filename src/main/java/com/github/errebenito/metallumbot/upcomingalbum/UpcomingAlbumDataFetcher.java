package com.github.errebenito.metallumbot.upcomingalbum;

import java.io.IOException;

public interface UpcomingAlbumDataFetcher {
    String fetch() throws IOException, InterruptedException;
}
