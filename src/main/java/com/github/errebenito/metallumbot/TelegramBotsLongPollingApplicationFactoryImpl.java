package com.github.errebenito.metallumbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class TelegramBotsLongPollingApplicationFactoryImpl implements TelegramBotsLongPollingApplicationFactory {
    @Override
    public TelegramBotsLongPollingApplication create() {
        return new TelegramBotsLongPollingApplication();
    }
}
