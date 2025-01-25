package com.github.errebenito.metallumbot.connector;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages connections to the metal-archives site.

 * @author rbenito
 *
 */
public class UrlConnector {
  
  private static final int TIMEOUT = 5000;
    
  private URL url;

  private static final Logger LOGGER = LoggerFactory.getLogger(UrlConnector.class);

    
  /**
   * Constructor.
   */
  public UrlConnector() {
    new UrlConnector(null);
  }
  
  private UrlConnector(final URL url) {
    this.url = url;   
  }
    
  /**
   * Specifies the URL associated to the connector.

   * @param url The URL to connect to.
   * @return an instance of UrlConnector
   * @throws MalformedURLException When the URL is not valid
   */
  public UrlConnector withUrl(final URL url) throws MalformedURLException {
    this.url = url;
    return new UrlConnector(url);
  }
  
  /**
   * Connects to the URL associated to the connector.

   * @return An instance of HttpURLConnection.
   * @throws IOException When an error occurs attempting to connect.
   */
  @SuppressFBWarnings
  public HttpURLConnection connect() throws IOException {
    // The URL comes from a list of valid URLs and therefore it is safe to connect.
    final HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
    connection.setReadTimeout(TIMEOUT);
    connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
    connection.addRequestProperty("User-Agent", "Mozilla");
    connection.addRequestProperty("Referer", "google.com");
    connection.setInstanceFollowRedirects(false);
    return connection;
  }
  
  /**
   * Reads the upcoming albums JSON.

   * @return A ByteArrayInputStream with the contents of the JSON.
   * @throws IOException When an error occurs while reading the JSON.
   */
  @SuppressFBWarnings
  public InputStream readUpcomingAlbumsJson() throws IOException {
    String result;
    // The URL comes from a list of valid URLs and therefore it is safe to connect.
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        new BufferedInputStream(this.url.openStream())))) {
      result = reader.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
       LOGGER.error(e.getMessage());
       throw e;
    }
    result = result.replace(": ,", ": 0,"); // fix unexpected empty values
    return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
  }
}
