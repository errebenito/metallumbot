package com.github.errebenito.metallumbot.upcomingalbum;

class InterruptingUpcomingAlbumProvider implements RandomUpcomingAlbumProvider {

    @Override
    public Album getRandomUpcomingAlbum() throws InterruptedException {
        throw new InterruptedException("interrupted");
    }
}
