package com.github.errebenito.metallumbot.upcomingalbum;

class StubUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {

    private final Album album;

    StubUpcomingAlbumProvider(Album album) {
        this.album = album;
    }

    @Override
    public Album getRandomUpcomingAlbum() {
        return album;
    }
}
