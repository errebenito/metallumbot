package com.github.errebenito.metallumbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.github.errebenito.metallumbot.command.CommandRunner;
import com.github.errebenito.metallumbot.command.CommandRunnerFactory;
import com.github.errebenito.metallumbot.command.CommandRunnerFactoryImpl;

/**
 * Unit tests for the MetallumBot class.

 * @author rbenito
 *
 */
@ExtendWith(MockitoExtension.class)
class MetallumBotTest {
   
  @BeforeEach
  void setUp() {
    System.setProperty("https.protocols", "TLSv1.2");
  }

  @ParameterizedTest
  @ValueSource(strings = {"/band", "/upcoming"})
  void testOnUpdateReceived(final String command) throws MalformedURLException, TelegramApiException {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = new Chat();
    final long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    if("/band".equals(command)) {
      when(runnerMock.doBand()).thenReturn("Band");
    } else if ("/upcoming".equals(command)){
      when(runnerMock.doUpcoming()).thenReturn("Upcoming");
    }
    final MetallumBot bot = spy(new MetallumBot(factoryMock));
    doReturn(null).when(bot).execute(any(SendMessage.class));
    bot.onUpdateReceived(updateMock);
    verify(bot).sendMessage(any(SendMessage.class));
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, atLeastOnce()).getMessage();
  }
  
  @Test
  void testOnUpdateReceivedWithoutMessage() {
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(false);
    final MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl());
    bot.onUpdateReceived(updateMock);
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, never()).getMessage();
  }
  
  @Test
  void testOnUpdateReceivedWithEmptyMessage() {
    Message messageMock = mock(Message.class);
    when(messageMock.hasText()).thenReturn(false);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(messageMock);
    final MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl());
    bot.onUpdateReceived(updateMock);
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, atLeastOnce()).getMessage();
    verify(messageMock, atLeastOnce()).hasText();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/band", "/upcoming"})
  void testOnUpdateReceivedThrowsException(String command) throws MalformedURLException {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = new Chat();
    final long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    when(factoryMock.create(any(URL.class))).thenThrow(new MalformedURLException("Invalid URL"));
    final MetallumBot bot = new MetallumBot(factoryMock);
    bot.onUpdateReceived(updateMock);
    verify(updateMock, atLeastOnce()).getMessage();
    verify(factoryMock).create(any(URL.class));
  }

  @Test
  void testOnUpdateReceivedLogsErrorWhenExecuteFailsForStart() {
    final Message message = new Message();
    message.setText("/start");
    final Chat chat = new Chat();
    final long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    final Update updateMock =  mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl()) {
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        throw new TelegramApiException("Simulated failure");
      }
    };
    bot.onUpdateReceived(updateMock);
    verify(updateMock, atLeastOnce()).getMessage();
  }


  @Test
  void testDoBandCallsSendMessage() throws MalformedURLException, TelegramApiException {
    Message message = new Message();
    message.setText("/band");
    Chat chat = new Chat();
    long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    when(runnerMock.doBand()).thenReturn("Band Info");
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(factoryMock) {
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        captured[0] = message; // capture manually
      }
    };

    bot.onUpdateReceived(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doBand();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals("Band Info", sent.getText());
  }

  @Test
  void testDoUpcomingCallsSendMessage() throws MalformedURLException, TelegramApiException {
    Message message = new Message();
    message.setText("/upcoming");
    Chat chat = new Chat();
    long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    when(runnerMock.doUpcoming()).thenReturn("Upcoming Info");
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(factoryMock) {
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        captured[0] = message; // capture manually
      }
    };

    bot.onUpdateReceived(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doUpcoming();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals("Upcoming Info", sent.getText());
  }

  @Test
  void testDefaultCaseCallsSendMessage() throws TelegramApiException {
    Message message = new Message();
    message.setText("/invalid");
    Chat chat = new Chat();
    long chatId = Long.parseLong(System.getenv("CHAT_ID"));
    chat.setId(chatId);
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(null) { // no need for factory here
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        captured[0] = message;
      }
    };
    bot.onUpdateReceived(updateMock);
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertTrue(sent.getText().contains("Usage"));
  }
  
  @Test
  void testInitializeBotRegistersBot() throws Exception {
    try (MockedConstruction<TelegramBotsApi> constructorMock = mockConstruction(
        TelegramBotsApi.class, 
        (mock, context) -> {
          when(mock.registerBot(any(MetallumBot.class))).thenReturn(null);
        })) {
      MetallumBot.initializeBot();
      TelegramBotsApi constructed = constructorMock.constructed().get(0);
      verify(constructed).registerBot(any(MetallumBot.class));
    }
  }

  @Test
  void testInitializeBotHandlesTelegramApiExceptionWithLogging() throws Exception {
    LogCaptor logCaptor = LogCaptor.forClass(MetallumBot.class);
    try (MockedConstruction<TelegramBotsApi> ignored = mockConstruction(
      TelegramBotsApi.class,
        (mock, context) -> {
          doThrow(new TelegramApiException("test")).when(mock).registerBot(any());
        })) {
      MetallumBot.initializeBot();
    }
    List<String> logs = logCaptor.getErrorLogs();
    assertTrue(logs.stream().anyMatch(msg -> msg.contains("Error setting up and registering bot")),
               "Expected log message was not found");
    }

  @Test
  void testMainCallsInitializeBot() throws Exception {
    try (MockedConstruction<TelegramBotsApi> mocked = mockConstruction(
      TelegramBotsApi.class,
      (mock, context) -> {
        })) {
          MetallumBot.main(new String[]{});
          TelegramBotsApi botsApi = mocked.constructed().get(0);
          verify(botsApi).registerBot(any()); // or any(MetallumBot.class)
      }
  }
  
  @Test
  void testGetBotUsername() {
    final MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl());
    assertEquals(System.getenv("METALLUM_BOT_NAME"),
        bot.getBotUsername(), "Bot returned incorrect name");
  }
}
