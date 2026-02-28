package com.github.errebenito.metallumbot.messaging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TelegramMessageSenderTest {

    @Test
    void shouldCallExecuteWithCorrectMessage() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        sender.sendMessage("123", "hello");

        assertNotNull(fakeClient.capturedMessage);
        assertEquals("123", fakeClient.capturedMessage.getChatId());
        assertEquals("hello", fakeClient.capturedMessage.getText());
    }

    @Test
    void shouldWrapTelegramApiException() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        fakeClient.shouldThrow = true;

        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        MessageSendingException ex =
                assertThrows(MessageSendingException.class,
                        () -> sender.sendMessage("123", "hello"));

        assertEquals("boom", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    void shouldConstructSenderWithToken() {
        TelegramMessageSender sender = new TelegramMessageSender("token");
        assertNotNull(sender);
    }
}