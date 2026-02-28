package com.github.errebenito.metallumbot.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Verifies that the bot calls the band use case for the band command")
    void givenUpdateWithBandCommandWhenConsumingShouldDelegateToBandUseCase() {
        Update update = updateWithCommand("/band");

        bot.consume(update);

        assertTrue(band.called);
    }

    @Test
    @DisplayName("Verifies that the bot sends the response to the band command to the correct chat")
    void givenUpdateWithBandCommandWhenConsumingShouldSendResponseToExpectedChat() {
        Update update = updateWithCommand("/band");

        bot.consume(update);

        assertEquals("42", band.receivedChatId);
    }

    @Test
    @DisplayName("Verifies that the band command does not call the upcoming albums use case")
    void givenUpdateWithBandCommandWhenConsumingShouldNotCallUpcomingAlbumsUseCase() {
        Update update = updateWithCommand("/band");

        bot.consume(update);

        assertFalse(upcoming.called);
    }

    @Test
    @DisplayName("Verifies that the band command does not call the unknown command use case")
    void givenUpdateWithBandCommandWhenConsumingShouldNotCallUnknownCommandUseCase() {
        Update update = updateWithCommand("/band");

        bot.consume(update);

        assertFalse(unknown.called);
    }

    @Test
    @DisplayName("Verifies that the bot calls the upcoming albums use case for the upcoming command")
    void givenUpdateWithUpcomingCommandWhenConsumingShouldDelegateToUpcomingAlbumUseCase() {
        Update update = updateWithCommand("/upcoming");

        bot.consume(update);

        assertTrue(upcoming.called);
    }

    @Test
    @DisplayName("Verifies that the bot sends the response to the upcoming command to the correct chat")
    void givenUpdateWithUpcomingCommandWhenConsumingShouldSendResponseToExpectedChat() {
        Update update = updateWithCommand("/upcoming");

        bot.consume(update);

        assertEquals("42", upcoming.receivedChatId);

        assertFalse(band.called);
        assertFalse(unknown.called);
    }

    @Test
    @DisplayName("Verifies that the upcoming command does not call the band use case")
    void givenUpdateWithUpcomingCommandWhenConsumingShouldNotCallBandUseCase() {
        Update update = updateWithCommand("/upcoming");

        bot.consume(update);

        assertFalse(band.called);
    }

    @Test
    @DisplayName("Verifies that the upcoming command does not call the unknown command use case")
    void givenUpdateWithUpcomingCommandWhenConsumingShouldNotCallUnknownCommandUseCase() {
        Update update = updateWithCommand("/upcoming");

        bot.consume(update);

        assertFalse(unknown.called);
    }

    @Test
    @DisplayName("Verifies that other commands call the unknown command use case")
    void givenUpdateWithUnsupportedCommandWhenConsumingShouldDelegateToUnknownCommandUseCase() {
        Update update = updateWithCommand("/foo");

        bot.consume(update);

        assertTrue(unknown.called);
    }

    @Test
    @DisplayName("Verifies that an unknown command sends the response to the correct chat")
    void givenUpdateWithUnsupportedCommandWhenConsumingShouldSendResponseToExpectedChat() {
        Update update = updateWithCommand("/foo");

        bot.consume(update);

        assertEquals("42", unknown.receivedChatId);
    }

    @Test
    @DisplayName("Verifies that an unknown command does not call the band use case")
    void givenUpdateWithUnsupportedCommandWhenConsumingShouldNotCallBandUseCase() {
        Update update = updateWithCommand("/foo");

        bot.consume(update);

        assertFalse(band.called);
    }

    @Test
    @DisplayName("Verifies that an unknown command does not call the upcoming albums use case")
    void givenUpdateWithUnsupportedCommandWhenConsumingShouldNotCallUnknownCommandUseCase() {
        Update update = updateWithCommand("/foo");

        bot.consume(update);

        assertFalse(upcoming.called);
    }

    @Test
    @DisplayName("Verifies that an update without message does not call the band use case")
    void givenUpdateWithoutMessageWhenConsumingShouldNotCallBandUseCase() {
        Update update = new Update();

        bot.consume(update);

        assertFalse(band.called);
    }

    @Test
    @DisplayName("Verifies that an update without message does not call the upcoming albums use case")
    void givenUpdateWithoutMessageWhenConsumingShouldNotCallUpcomingAlbumUseCase() {
        Update update = new Update();

        bot.consume(update);

        assertFalse(upcoming.called);
    }

    @Test
    @DisplayName("Verifies that an update without message does not call the unknown command use case")
    void givenUpdateWithoutMessageWhenConsumingShouldNotCallUnknownCommandUseCase() {
        Update update = new Update();

        bot.consume(update);

        assertFalse(unknown.called);
    }

    @Test
    @DisplayName("Verifies that an update with an empty message does not call the band use case")
    void givenUpdateWithEmptyMessageWhenConsumingShouldNotCallBandUseCase() {
        Chat chat = Chat.builder().id(42L).type("private").build();
        Message message = Message.builder().chat(chat).text(null).build();
        Update update = new Update();
        update.setMessage(message);

        bot.consume(update);

        
        assertFalse(band.called);
    }

    @Test
    @DisplayName("Verifies that an update with an empty message does not call the upcoming albums use case")
    void givenUpdateWithEmptyMessageWhenConsumingShouldNotCallUpcomingAlbumUseCase() {
        Chat chat = Chat.builder().id(42L).type("private").build();
        Message message = Message.builder().chat(chat).text(null).build();
        Update update = new Update();
        update.setMessage(message);

        bot.consume(update);

        
        assertFalse(upcoming.called);
    }

    @Test
    @DisplayName("Verifies that an update with an empty message does not call the unknown command use case")
    void givenUpdateWithEmptyMessageWhenConsumingShouldNotCallUnknownCommandUseCase() {
        Chat chat = Chat.builder().id(42L).type("private").build();
        Message message = Message.builder().chat(chat).text(null).build();
        Update update = new Update();
        update.setMessage(message);

        bot.consume(update);

        
        assertFalse(unknown.called);
    }
}