package com.github.errebenito.metallumbot.messaging;

import org.telegram.telegrambots.client.AbstractTelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessageSender implements MessageSending {
    private final AbstractTelegramClient client;

    public TelegramMessageSender(AbstractTelegramClient client) {
        this.client = client;
    }

    public TelegramMessageSender(String botToken) {
        this(new OkHttpTelegramClient(botToken));
    }

    @Override
    public void sendMessage(String chatId, String text) throws MessageSendingException {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            client.execute(message);
        } catch (TelegramApiException e) {
            throw new MessageSendingException(e.getMessage(), e);
        }
    }
}
