package com.github.errebenito.metallumbot.bot;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.github.errebenito.metallumbot.model.Command;

class MetallumBotTest {

    @Test
    @DisplayName("Verifies that the bot delegates command processing")
    void givenBotReceivesUpdateWithMessageWhenHandlingItThenShouldDelegateToCommandProcessor() {
        CommandProcessor processor = mock(CommandProcessor.class);
        MetallumBot bot = new MetallumBot(processor);

        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);
        when(message.getChatId()).thenReturn(42L);
        when(message.getText()).thenReturn("/band");

        bot.consume(update);

        verify(processor).process(new Command("42", "/band"));
    }

    @Test
    @DisplayName("Verifies that the bot does not delegate if the update is not a message")
    void givenBotReceivesUpdateWithoutMessageWhenHandlingItThenShouldNotDelegateToCommandProcessor() {
        CommandProcessor processor = mock(CommandProcessor.class);
        MetallumBot bot = new MetallumBot(processor);

        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(false);

        bot.consume(update);

        verifyNoInteractions(processor);
    }

    @Test
    @DisplayName("Verifies that the bot does not delegate if the update is a message without text")
    void givenBotReceivesUpdateWithoutMessageTextWhenHandlingItThenShouldNotDelegateToCommandProcessor() {
        CommandProcessor processor = mock(CommandProcessor.class);
        MetallumBot bot = new MetallumBot(processor);

        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(false);

        bot.consume(update);

        verifyNoInteractions(processor);
    }
}
