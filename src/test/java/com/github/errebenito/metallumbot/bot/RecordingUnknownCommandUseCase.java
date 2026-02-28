package com.github.errebenito.metallumbot.bot;

import com.github.errebenito.metallumbot.unknowncommand.UnknownCommandUseCase;

class RecordingUnknownCommandUseCase implements UnknownCommandUseCase {

    String receivedChatId;
    boolean called = false;

    @Override
    public void sendInstructions(String chatId) {
        this.called = true;
        this.receivedChatId = chatId;
    }
}