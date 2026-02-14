package com.github.errebenito.metallumbot.bot;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import com.github.errebenito.metallumbot.model.Command;

class MetallumBotTest {
    @Test
    void shouldDelegateToProcessorWhenTextMessageReceived() {

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
    void shouldNotCallProcessorWhenNoMessage() {

        CommandProcessor processor = mock(CommandProcessor.class);
        MetallumBot bot = new MetallumBot(processor);

        Update update = mock(Update.class);
        when(update.hasMessage()).thenReturn(false);

        bot.consume(update);

        verifyNoInteractions(processor);
    }

    @Test
    void shouldNotCallProcessorWhenMessageHasNoText() {

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
