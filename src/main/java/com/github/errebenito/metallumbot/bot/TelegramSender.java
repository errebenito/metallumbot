package com.github.errebenito.metallumbot.bot;

public interface TelegramSender {
    void sendMessage(String chatId, String text) throws Exception;
}
