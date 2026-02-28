package com.github.errebenito.metallumbot.messaging;

import java.io.Serializable;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FakeTelegramClient extends OkHttpTelegramClient {
    SendMessage capturedMessage;
    boolean shouldThrow;

    FakeTelegramClient() {
        super("dummy");
    }

    @Override
    public <T extends Serializable, M extends BotApiMethod<T>> T execute(M method)
            throws TelegramApiException {

        this.capturedMessage = (SendMessage) method;

        if (shouldThrow) {
            throw new TelegramApiException("boom");
        }

        return null;
    }
}
