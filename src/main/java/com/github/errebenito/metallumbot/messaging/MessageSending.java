package com.github.errebenito.metallumbot.messaging;

public interface MessageSending {
    void sendMessage(String chatId, String text) throws MessageSendingException;
}
