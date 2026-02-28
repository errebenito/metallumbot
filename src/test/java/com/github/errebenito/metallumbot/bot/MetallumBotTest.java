package com.github.errebenito.metallumbot.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

class MetallumBotTest {

    RecordingUnknownCommandUseCase unknown = new RecordingUnknownCommandUseCase();
    RecordingRandomBandUseCase band = new RecordingRandomBandUseCase();
    RecordingRandomUpcomingAlbumUseCase upcoming = new RecordingRandomUpcomingAlbumUseCase();

    MetallumBot bot = new MetallumBot(unknown, band, upcoming);

    private Update updateWithCommand(String command) {
        Chat chat = Chat.builder().id(42L).type("private").build();
        Message message = Message.builder().chat(chat).text(command).build();
        Update update = new Update();

        update.setMessage(message);

        return update;
    }

    @Test
    void shouldDispatchBandCommand() {
        Update update = updateWithCommand("/band");

        bot.consume(update);

        assertTrue(band.called);
        assertEquals("42", band.receivedChatId);

        assertFalse(upcoming.called);
        assertFalse(unknown.called);
    }

    @Test
    void shouldDispatchUpcommingCommand() {
        Update update = updateWithCommand("/upcoming");

        bot.consume(update);

        assertTrue(upcoming.called);
        assertEquals("42", upcoming.receivedChatId);

        assertFalse(band.called);
        assertFalse(unknown.called);
    }


    @Test
    void shouldHandleUnknownCommand() {
        Update update = updateWithCommand("/foo");

        bot.consume(update);

        assertTrue(unknown.called);
        assertEquals("42", unknown.receivedChatId);

        assertFalse(band.called);
        assertFalse(upcoming.called);
    }

    @Test
    void shouldIgnoreUpdateWithoutMessage() {
        Update update = new Update();

        bot.consume(update);

        assertFalse(band.called);
        assertFalse(upcoming.called);
        assertFalse(unknown.called);
    }

    @Test
    void shouldIgnoreUpdateWithMessageWithoutText() {
        Chat chat = Chat.builder().id(42L).type("private").build();
        Message message = Message.builder().chat(chat).text(null).build();
        Update update = new Update();
        update.setMessage(message);

        bot.consume(update);

        
        assertFalse(band.called);
        assertFalse(upcoming.called);
        assertFalse(unknown.called);
    }
}