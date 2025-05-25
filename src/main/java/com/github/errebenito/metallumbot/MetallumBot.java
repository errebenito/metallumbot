package com.github.errebenito.metallumbot;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import com.github.errebenito.metallumbot.command.CommandRunner;
import com.github.errebenito.metallumbot.command.CommandRunnerFactory;
import com.github.errebenito.metallumbot.command.CommandRunnerFactoryImpl;
import com.github.errebenito.metallumbot.connector.UrlType;
import com.github.errebenito.metallumbot.utils.MessageUtils;

/**
 * Main bot class.

 * @author rbenito
 *
 */

public class MetallumBot implements LongPollingSingleThreadUpdateConsumer {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MetallumBot.class);

  private static final String ERROR_MESSAGE = "Error sending message";

  private static final String USAGE = """
  Usage:

      /band Returns a random band
      /upcoming Returns a partial list of upcoming releases""";

  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");

  private final CommandRunnerFactory factory;
  
  private final TelegramClient client;

  /**
   * Constructor.
   */
  public MetallumBot(CommandRunnerFactory factory, TelegramClient client) {
    this.factory = factory;
    this.client = client;
  }
  
  /**
   * Constructor.
   */
  public MetallumBot(CommandRunnerFactory factory) {
    this(factory, new OkHttpTelegramClient(TOKEN));
  }
  /**
   * Method for receiving messages.

   * @param update Contains a message from the user.
   */
  @Override
  public void consume(final Update update) {
    CommandRunner runner;
    if (update.hasMessage() && update.getMessage().hasText()) {
      final String messageText = update.getMessage().getText();
      switch (messageText) { 
        case "/band" -> {
          try {
            runner = this.factory.create(UrlType.RANDOM_BAND.getUrl());
            sendMessage(MessageUtils.generateMessage(update.getMessage().getChatId().toString(), runner.doBand()));
          } catch (TelegramApiException | MalformedURLException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        case "/upcoming" -> {
          try {
            runner = this.factory.create(UrlType.UPCOMING_RELEASES.getUrl());
            sendMessage(MessageUtils.generateMessage(update.getMessage().getChatId().toString(),
                runner.doUpcoming()));
          } catch (TelegramApiException | MalformedURLException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        default -> {
          try {
            sendMessage(MessageUtils.generateMessage(update.getMessage().getChatId().toString(), USAGE));
          } catch (TelegramApiException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
      }
    }
  }
  
  protected void sendMessage(SendMessage message) throws TelegramApiException {
    client.execute(message);
  }

  /**
   * Main method.

   * @param args The arguments.
   */
  public static void main(final String[] args) {
    initializeBot();
  }
  
  static void initializeBot() {
    System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
    try (TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication()) {
      bot.registerBot(TOKEN, new MetallumBot(new CommandRunnerFactoryImpl()));
    } catch (TelegramApiException e) {
      LOGGER.error("Error setting up and registering bot", e);
    }
  }
}
