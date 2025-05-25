package com.github.errebenito.metallumbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
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
import java.util.List;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
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
  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");
  
  @BeforeEach
  void setUp() {
    System.setProperty("https.protocols", "TLSv1.2");
  }

  @ParameterizedTest
  @ValueSource(strings = {"/band", "/upcoming"})
  void testOnUpdateReceived(final String command) throws MalformedURLException, TelegramApiException {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
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
    doNothing().when(bot).sendMessage(any(SendMessage.class));
    bot.consume(updateMock);
    verify(bot).sendMessage(any(SendMessage.class));
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, atLeastOnce()).getMessage();
  }
  
  @Test
  void testSendMessageExecutesMessage() throws TelegramApiException {
    final Message message = new Message();
    message.setText("test");
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
    message.setChat(chat);
    SendMessage sendMessage = new SendMessage(message.getChatId().toString(), message.getText());
    TelegramClient telegramClientMock = mock(OkHttpTelegramClient.class);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    MetallumBot metallumBot = new MetallumBot(factoryMock, telegramClientMock);

    metallumBot.sendMessage(sendMessage);

    verify(telegramClientMock).execute(sendMessage);
  }

  @Test
  void testOnUpdateReceivedWithoutMessage() {
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(false);
    final MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl());
    bot.consume(updateMock);
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
    bot.consume(updateMock);
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, atLeastOnce()).getMessage();
    verify(messageMock, atLeastOnce()).hasText();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/band", "/upcoming"})
  void testOnUpdateReceivedThrowsException(String command) throws MalformedURLException {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    when(factoryMock.create(any(URL.class))).thenThrow(new MalformedURLException("Invalid URL"));
    final MetallumBot bot = new MetallumBot(factoryMock);
    bot.consume(updateMock);
    verify(updateMock, atLeastOnce()).getMessage();
    verify(factoryMock).create(any(URL.class));
  }

  @Test
  void testOnUpdateReceivedLogsErrorWhenExecuteFailsForStart() {
    final Message message = new Message();
    message.setText("/start");
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
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
    bot.consume(updateMock);
    verify(updateMock, atLeastOnce()).getMessage();
  }


  @Test
  void testDoBandCallsSendMessage() throws MalformedURLException {
    Message message = new Message();
    message.setText("/band");
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
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

    bot.consume(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doBand();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals("Band Info", sent.getText());
  }

  @Test
  void testDoUpcomingCallsSendMessage() throws MalformedURLException {
    Message message = new Message();
    message.setText("/upcoming");
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
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

    bot.consume(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doUpcoming();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals("Upcoming Info", sent.getText());
  }

  @Test
  void testDefaultCaseCallsSendMessage() {
    Message message = new Message();
    message.setText("/invalid");
    final Chat chat = Chat.builder().id(Long.parseLong(System.getenv("CHAT_ID"))).type("private").build();
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
    bot.consume(updateMock);
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertTrue(sent.getText().contains("Usage"));
  }
  
  @Test
  void testInitializeBotRegistersBot() throws Exception {
    try (MockedConstruction<TelegramBotsLongPollingApplication> constructorMock = mockConstruction(
        TelegramBotsLongPollingApplication.class, 
        (mock, context) -> {
          when(mock.registerBot(eq(TOKEN), any(MetallumBot.class))).thenReturn(null);
        })) {
      MetallumBot.initializeBot();
      TelegramBotsLongPollingApplication constructed = constructorMock.constructed().get(0);
      verify(constructed).registerBot(eq(TOKEN), any(MetallumBot.class));
    }
  }

  @Test
  void testInitializeBotHandlesTelegramApiExceptionWithLogging() {
    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    LoggerConfig loggerConfig = loggerContext.getConfiguration().getLoggerConfig(MetallumBot.class.getName());

    // Create a ListAppender and start it
    TestListAppender appender = TestListAppender.create("TestListAppender");
    appender.start();

    // Add appender to logger config
    loggerConfig.addAppender(appender, null, null);
    loggerContext.updateLoggers(); // Important to update
    try (var _ = mockConstruction(TelegramBotsLongPollingApplication.class, (mock, context) -> {
        when(mock.registerBot(eq(TOKEN), any())).thenThrow(new TelegramApiException("test"));
      })) {
      MetallumBot.initializeBot();
    }
    List<String> errorMessages = appender.getFormattedMessages();

    assertTrue(errorMessages.stream().anyMatch(msg -> msg.contains("Error setting up and registering bot")),
               "Expected log message was not found");

    // Remove the appender after test
    loggerConfig.removeAppender("TestListAppender");
    appender.stop();
    loggerContext.updateLoggers();
  }

  @Test
  void testMainCallsInitializeBot() throws Exception {
    try (MockedConstruction<TelegramBotsLongPollingApplication> mocked = mockConstruction(
        TelegramBotsLongPollingApplication.class,
      (mock, context) -> {
        })) {
          MetallumBot.main(new String[]{});
          TelegramBotsLongPollingApplication bot = mocked.constructed().get(0);
          verify(bot).registerBot(eq(TOKEN), any());
      }
  }
}
