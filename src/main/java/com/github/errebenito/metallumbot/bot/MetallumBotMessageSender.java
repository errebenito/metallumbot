package com.github.errebenito.metallumbot.bot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MetallumBotMessageSender implements TelegramSender {
    private final OkHttpTelegramClient client;

    public MetallumBotMessageSender(String botToken) {
        this.client = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void sendMessage(String chatId, String text) throws Exception {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        client.execute(message);
    }
}
