package com.github.errebenito.metallumbot.messaging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TelegramMessageSenderTest {

    @Test
    @DisplayName("Verifies that the message sender sends messages")
    void givenChatIdAndTextWhenSendingMessageThenShouldSendMessage() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        sender.sendMessage("123", "hello");

        assertNotNull(fakeClient.capturedMessage);
    }

    @Test
    @DisplayName("Verifies that the message sender sends messages to the correct recipient")
    void givenChatIdAndTextWhenSendingMessageThenShouldSendMessageToExpectedChatId() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        sender.sendMessage("123", "hello");

        assertEquals("123", fakeClient.capturedMessage.getChatId());
    }

    @Test
    @DisplayName("Verifies that the message sender sends messages with the expected text")
    void givenChatIdAndTextWhenSendingMessageThenShouldSendMessageWithExpectedText() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        sender.sendMessage("123", "hello");

        assertEquals("hello", fakeClient.capturedMessage.getText());
    }

    @Test
    @DisplayName("Verifies that wrapping exceptions in a custom exception preserves the original message")
    void givenExceptionIsThrownWhenSendingMessageThenShouldWrapInCustomExceptionPreservingMessage() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        fakeClient.shouldThrow = true;

        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        MessageSendingException ex =
                assertThrows(MessageSendingException.class,
                        () -> sender.sendMessage("123", "hello"));

        assertEquals("boom", ex.getMessage());
    }

    @Test
    @DisplayName("Verifies that wrapping exceptions in a custom exception preserves the original cause")
    void givenExceptionIsThrownWhenSendingMessageThenShouldWrapInCustomExceptionPreservingCause() {
        FakeTelegramClient fakeClient = new FakeTelegramClient();
        fakeClient.shouldThrow = true;

        TelegramMessageSender sender = new TelegramMessageSender(fakeClient);

        MessageSendingException ex =
                assertThrows(MessageSendingException.class,
                        () -> sender.sendMessage("123", "hello"));

        assertNotNull(ex.getCause());
    }

    @Test
    @DisplayName("Verifies that constructing a message sender works")
    void givenTokenWhenConstructinSenderThenShouldNotReturnNull() {
        TelegramMessageSender sender = new TelegramMessageSender("token");
        assertNotNull(sender);
    }
}