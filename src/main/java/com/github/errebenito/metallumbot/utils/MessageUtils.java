package com.github.errebenito.metallumbot.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Utility methods for messages.

 * @author rbenito
 *
 */
public final class MessageUtils {

  private MessageUtils() {
    // Intentionally empty
  }
  
  /**
   * Returns a command to send a text message to a chat.

   * @param chatId The id of the chat where the message will be sent.
   * @param text The content of the message.
   * @return An instance of SendMessage.
   */
  public static SendMessage generateMessage(final String chatId, final String text) {
    return new SendMessage(chatId, text);
  }
}
