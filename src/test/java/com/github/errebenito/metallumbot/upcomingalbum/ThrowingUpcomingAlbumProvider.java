package com.github.errebenito.metallumbot.upcomingalbum;

class ThrowingUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {

    @Override
    public Album getRandomUpcomingAlbum() {
        throw new RuntimeException("boom");
    }
}
