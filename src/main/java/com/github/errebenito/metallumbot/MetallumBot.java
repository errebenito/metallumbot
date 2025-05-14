package com.github.errebenito.metallumbot;

import com.github.errebenito.metallumbot.command.CommandRunner;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;
import com.github.errebenito.metallumbot.utils.MessageUtils;
import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Main bot class.

 * @author rbenito
 *
 */

public class MetallumBot extends TelegramLongPollingBot {
  
  private static final String ERROR_MESSAGE = "Error sending message";

  private static final Logger LOGGER = LoggerFactory.getLogger(MetallumBot.class);

  private static final String USAGE = """
  Usage:
   
      /band Returns a random band
      /upcoming Returns a partial list of upcoming releases""";
  
  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");
  
  private static final String NAME = System.getenv("METALLUM_BOT_NAME");  
    
  /**
   * Constructor.
   */
  public MetallumBot() {
    super(TOKEN);
  }
  
  /**
   * Method for receiving messages.

   * @param update Contains a message from the user.
   */
  @Override
  public void onUpdateReceived(final Update update) {
    CommandRunner runner;
    if (update.hasMessage() && update.getMessage().hasText()) {
      final String messageText = update.getMessage().getText();
      switch (messageText) { 
        case "/band" -> {
          try {
            runner = new CommandRunner(new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl()));
            execute(MessageUtils.generateMessage(update.getMessage().getChatId(), runner.doBand()));
          } catch (TelegramApiException | MalformedURLException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        case "/upcoming" -> {
          try {
            runner = new CommandRunner(new UrlConnector()
                .withUrl(UrlType.UPCOMING_RELEASES.getUrl()));
            execute(MessageUtils.generateMessage(update.getMessage().getChatId(), 
                runner.doUpcoming()));
          } catch (TelegramApiException | MalformedURLException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        default -> {
          try {
            execute(MessageUtils.generateMessage(update.getMessage().getChatId(), USAGE));
          } catch (TelegramApiException _) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
      }
    }
  }
      
  /**
   * Returns the bot name which was specified during registration.

   * @return The bot name
   */
  @Override
  public String getBotUsername() {
    return NAME;
  }

  /**
   * Main method.

   * @param args The arguments.
   */
  public static void main(final String[] args) {

    TelegramBotsApi botsApi;

    try {
      System.setProperty("https.protocols", "TLSv1.2, TLSV1.3");
      botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(new MetallumBot());
    } catch (TelegramApiException _) {
      LOGGER.error("Error setting up and registering bot");
    }
  }
}