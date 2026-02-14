package com.github.errebenito.metallumbot.bot;

public class FakeTelegramSender implements TelegramSender {
    String lastChatId;
    String lastText;

    @Override
    public void sendMessage(String chatId, String text) {
        this.lastChatId = chatId;
        this.lastText = text;
    }
}
