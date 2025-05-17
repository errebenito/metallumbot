package com.github.errebenito.metallumbot.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Unit tests for the MessageUtils class.

 * @author rbenito
 *
 */
class MessageUtilsTest {

  @Test
  void testGeneratedMessageContainsCorrectChatId() {
    final SendMessage message = MessageUtils.generateMessage(1L, "message");
    assertEquals("1", message.getChatId(), "The message contains the wrong chatId");
  }
}
