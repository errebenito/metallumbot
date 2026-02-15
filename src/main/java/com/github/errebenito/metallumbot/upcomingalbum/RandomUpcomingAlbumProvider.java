package com.github.errebenito.metallumbot.upcomingalbum;

import com.github.errebenito.metallumbot.model.Album;

public interface RandomUpcomingAlbumProvider {
    Album getRandomUpcomingAlbum() throws Exception;
}
