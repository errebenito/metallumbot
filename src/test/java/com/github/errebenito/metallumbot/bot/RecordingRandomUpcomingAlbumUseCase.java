package com.github.errebenito.metallumbot.bot;

import com.github.errebenito.metallumbot.upcomingalbum.RandomUpcomingAlbumUseCase;

class RecordingRandomUpcomingAlbumUseCase implements RandomUpcomingAlbumUseCase {

    String receivedChatId;
    boolean called = false;

    @Override
    public void sendUpcomingAlbum(String chatId) {
        this.called = true;
        this.receivedChatId = chatId;
    }
}