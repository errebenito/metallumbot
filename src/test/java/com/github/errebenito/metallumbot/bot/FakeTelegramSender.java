package com.github.errebenito.metallumbot.bot;

import org.checkerframework.checker.units.qual.s;

public class FakeTelegramSender implements TelegramSender {
    String lastChatId;
    String lastText;
    private final boolean shouldThrow;

    public FakeTelegramSender() {
        this(false);
    }

    public FakeTelegramSender(boolean shouldThrow) {
        this.shouldThrow = shouldThrow;
    }

    @Override
    public void sendMessage(String chatId, String text) {
        if (shouldThrow) {
            throw new RuntimeException("simulated failure");
        }
        this.lastChatId = chatId;
        this.lastText = text;
    }
}
