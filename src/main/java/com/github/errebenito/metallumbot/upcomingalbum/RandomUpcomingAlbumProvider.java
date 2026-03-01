package com.github.errebenito.metallumbot.upcomingalbum;

import java.io.IOException;

public interface RandomUpcomingAlbumProvider {
    Album getRandomUpcomingAlbum() throws IOException, InterruptedException;
}
