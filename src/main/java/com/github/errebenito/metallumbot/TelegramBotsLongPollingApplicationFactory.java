package com.github.errebenito.metallumbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public interface TelegramBotsLongPollingApplicationFactory {
  TelegramBotsLongPollingApplication create();
}
