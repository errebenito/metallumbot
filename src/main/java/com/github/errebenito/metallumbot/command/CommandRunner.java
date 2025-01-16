package com.github.errebenito.metallumbot.command;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.model.UpcomingAlbums;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runner for the various commands supported by the bot.

 * @author rbenito
 *
 */
public class CommandRunner {
  private static final String LOCATION_HEADER = "Location";
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunner.class);
  
  private static final String ERROR_MESSAGE = "Error retrieving data. Returning default value:\n\n";

  private static final String DEFAULT_BAND = "https://www.metal-archives.com/bands/Black_Sabbath/99";
  
  private static final String DEFAULT_ALBUM = "https://www.metal-archives.com/albums/Black_Sabbath/Black_Sabbath/482";
  
  private final UrlConnector connector;
  
  /**
   * Constructor.

   * @param connector The UrlConnector
   */
  public CommandRunner(final UrlConnector connector) {
    this.connector = connector;
  }
  
  /**
   * Retrieves the URL to a random band.

   * @return A string representation of the URL.
   */
  @Nullable
  public String doBand() {
    String result = ERROR_MESSAGE + DEFAULT_BAND;
    try {
      result = connector.connect().getHeaderField(LOCATION_HEADER);
      
    } catch (IOException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
    return result;
  }
  
  /**
   * Retrieves the first 10 upcoming albums.

   * @return A string representation of links to the first 10 upcoming albums.
   */
  public String doUpcoming() {
    String result = ERROR_MESSAGE + DEFAULT_ALBUM;
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
      final UpcomingAlbums albums = objectMapper.readValue(connector.readUpcomingAlbumsJson(), 
          UpcomingAlbums.class); 
      result = albums.toString();
    } catch (IOException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
    return result;
  }
}
