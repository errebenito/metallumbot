package com.github.errebenito.metallumbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
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
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.BotSession;
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
  private static final String DEFAULT_ALBUM = "https://www.metal-archives.com/albums/Black_Sabbath/Black_Sabbath/482";
  private static final String DEFAULT_BAND = "https://www.metal-archives.com/bands/Black%20Sabbath";
  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");
  
  @BeforeEach
  void setUp() {
    System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
  }

  @ParameterizedTest
  @ValueSource(strings = {"/band", "/upcoming"})
  void testConsume(final String command) throws MalformedURLException, TelegramApiException {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    if("/band".equals(command)) {
      when(runnerMock.doBand()).thenReturn(DEFAULT_BAND);
    } else if ("/upcoming".equals(command)){
      when(runnerMock.doUpcoming()).thenReturn(DEFAULT_ALBUM);
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
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    SendMessage sendMessage = new SendMessage(message.getChatId().toString(), message.getText());
    TelegramClient telegramClientMock = mock(OkHttpTelegramClient.class);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    MetallumBot metallumBot = new MetallumBot(factoryMock, telegramClientMock);

    metallumBot.sendMessage(sendMessage);

    verify(telegramClientMock).execute(sendMessage);
  }

  @Test
  void testConsumeWithoutMessage() {
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(false);
    final MetallumBot bot = new MetallumBot(new CommandRunnerFactoryImpl());
    bot.consume(updateMock);
    verify(updateMock, atLeastOnce()).hasMessage();
    verify(updateMock, never()).getMessage();
  }
  
  @Test
  void testConsumeWithEmptyMessage() {
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
  void testConsumeThrowsException(String command) throws MalformedURLException {
    TestListAppender appender = attachTestAppender(MetallumBot.class);
    final Message message = new Message();
    message.setText(command);
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    when(factoryMock.create(any(URL.class))).thenThrow(new MalformedURLException("Invalid URL"));
    final MetallumBot bot = new MetallumBot(factoryMock);
    bot.consume(updateMock);
    List<String> messages = appender.getFormattedMessages();
    assertTrue(messages.stream().anyMatch(msg -> msg.contains("Error sending message")),
               "Expected log message for invalid URL was not found");
    verify(updateMock, atLeastOnce()).getMessage();
    verify(factoryMock).create(any(URL.class));
    appender.stop();
    ((LoggerContext) LogManager.getContext(false)).updateLoggers();
  }

  @Test
  void testConsumeLogsErrorWhenExecuteFailsForStart() {
    TestListAppender appender = attachTestAppender(MetallumBot.class);
    final Message message = new Message();
    message.setText("/start");
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
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
    List<String> messages = appender.getFormattedMessages();
    assertTrue(messages.stream().anyMatch(msg -> msg.contains("Error sending message")),
               "Expected log message for simulated failure was not found");
    verify(updateMock, atLeastOnce()).getMessage();
    appender.stop();
    ((LoggerContext) LogManager.getContext(false)).updateLoggers();
  }


  @Test
  void testDoBandCallsSendMessage() throws MalformedURLException {
    Message message = new Message();
    message.setText("/band");
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    when(runnerMock.doBand()).thenReturn(DEFAULT_BAND);
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(factoryMock) {
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        captured[0] = message;
      }
    };

    bot.consume(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doBand();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals(DEFAULT_BAND, sent.getText());
  }

  @Test
  void testDoUpcomingCallsSendMessage() throws MalformedURLException {
    Message message = new Message();
    message.setText("/upcoming");
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    CommandRunnerFactory factoryMock = mock(CommandRunnerFactory.class);
    CommandRunner runnerMock = mock(CommandRunner.class);
    when(factoryMock.create(any(URL.class))).thenReturn(runnerMock);
    when(runnerMock.doUpcoming()).thenReturn(DEFAULT_ALBUM);
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(factoryMock) {
      @Override
      protected void sendMessage(SendMessage message) throws TelegramApiException {
        captured[0] = message;
      }
    };

    bot.consume(updateMock);
    verify(factoryMock).create(any(URL.class));
    verify(runnerMock).doUpcoming();
    verify(updateMock, atLeastOnce()).getMessage();
    SendMessage sent = captured[0];
    assertNotNull(sent);
    assertEquals(DEFAULT_ALBUM, sent.getText());
  }

  @Test
  void testDefaultCaseCallsSendMessage() {
    Message message = new Message();
    message.setText("/invalid");
    final Chat chat = Chat.builder().id(Long.parseLong("1")).type("private").build();
    message.setChat(chat);
    Update updateMock = mock(Update.class);
    when(updateMock.hasMessage()).thenReturn(true);
    when(updateMock.getMessage()).thenReturn(message);
    final SendMessage[] captured = new SendMessage[1];
    MetallumBot bot = new MetallumBot(null) {
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
    TelegramBotsLongPollingApplication botMock = mock(TelegramBotsLongPollingApplication.class);
    when(botMock.registerBot(eq(TOKEN), any(MetallumBot.class))).thenReturn(mock(BotSession.class));

    TelegramBotsLongPollingApplicationFactory factory = () -> botMock;
    MetallumBot.initializeBot(factory);

    verify(botMock).registerBot(eq(TOKEN), any(MetallumBot.class));
  }

  @Test
  void testInitializeBotHandlesTelegramApiExceptionWithLogging() {
    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);

    TestListAppender appender = attachTestAppender(MetallumBot.class);

    try (var _ = mockConstruction(
      TelegramBotsLongPollingApplication.class,
      (mock, _) -> {
        when(mock.registerBot(eq(TOKEN), any())).thenThrow(new TelegramApiException("test"));
      })) {
      MetallumBot.initializeBot();
    }

    List<String> messages = appender.getFormattedMessages();
    assertTrue(messages.stream().anyMatch(msg -> msg.contains("Error setting up and registering bot")),
             "Expected log message was not found");

    appender.stop();
    loggerContext.updateLoggers();
  }

  @Test
  void testInitializeBotHandlesGenericException() {
    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    TestListAppender appender = attachTestAppender(MetallumBot.class);

    TelegramBotsLongPollingApplicationFactory factory = mock(TelegramBotsLongPollingApplicationFactory.class);
    when(factory.create()).thenThrow(new RuntimeException("Simulated generic exception"));

    MetallumBot.initializeBot(factory);

    // bot is null, so closeQuietly won't call close

    List<String> messages = appender.getFormattedMessages();
    assertTrue(messages.stream()
      .anyMatch(msg -> msg.contains("Unexpected exception during bot initialization")),
      "Expected log message for generic exception not found");

    appender.stop();
    loggerContext.updateLoggers();
  }

  @Test
  void testMainCallsInitializeBot() throws Exception {
    try (MockedConstruction<TelegramBotsLongPollingApplication> mocked = mockConstruction(
        TelegramBotsLongPollingApplication.class,
      (_, _) -> {
        })) {
          MetallumBot.main(new String[]{});
          TelegramBotsLongPollingApplication bot = mocked.constructed().get(0);
          verify(bot).registerBot(eq(TOKEN), any());
      }
  }

  @Test
  void testCloseQuietlyLogsWarningOnException() throws Exception {
    LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    TestListAppender appender = attachTestAppender(MetallumBot.class);

    TelegramBotsLongPollingApplication mockBot = mock(TelegramBotsLongPollingApplication.class);
    TelegramBotsLongPollingApplicationFactory factory = mock(TelegramBotsLongPollingApplicationFactory.class);

    when(factory.create()).thenReturn(mockBot);
    // Simulate registerBot throwing an exception, so registered = false and closeQuietly called
    when(mockBot.registerBot(eq(System.getenv("METALLUM_BOT_TOKEN")), any(MetallumBot.class)))
      .thenThrow(new RuntimeException("Simulated registerBot failure"));
    // Simulate close() throwing exception
    doThrow(new RuntimeException("Exception while closing bot")).when(mockBot).close();

    MetallumBot.initializeBot(factory);

    List<String> messages = appender.getFormattedMessages();
    // Should contain the warning log about exception while closing bot
    boolean foundWarn = messages.stream().anyMatch(msg -> msg.contains("Exception while closing bot"));
    assertTrue(foundWarn, "Expected warning log message for exception during close not found");

    appender.stop();
    loggerContext.updateLoggers();
  }

  private TestListAppender attachTestAppender(Class<?> clazz) {
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    Configuration config = ctx.getConfiguration();
    String loggerName = clazz.getName();

    TestListAppender appender = TestListAppender.create("TestListAppender");
    appender.start();

    LoggerConfig loggerConfig = new LoggerConfig(loggerName, Level.ALL, true);
    loggerConfig.addAppender(appender, Level.ALL, null);

    config.removeLogger(loggerName);

    config.addLogger(loggerName, loggerConfig);

    ctx.updateLoggers();
    return appender;
  }
}
