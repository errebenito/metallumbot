package com.github.errebenito.metallumbot.bot;

import com.github.errebenito.metallumbot.randomband.RandomBandUseCase;

class RecordingRandomBandUseCase implements RandomBandUseCase {

    String receivedChatId;
    boolean called = false;

    @Override
    public void sendBand(String chatId) {
        this.called = true;
        this.receivedChatId = chatId;
    }
}