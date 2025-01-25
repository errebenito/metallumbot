package com.github.errebenito.metallumbot.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UrlConnector class. 

 * @author rbenito
 *
 */
class UrlConnectorTest { 

  private static final String USER_AGENT =
  "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36";
  private static UrlConnector connector;
  
  @Test
  void whenRandomBandUrlShouldReturnCorrectUrl() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl());
    assertEquals(UrlType.RANDOM_BAND.getUrl(), 
        connector.connect().getURL(), "URL did not match");
  }
 
  @Test
  void whenUpcomingReleasesUrlShouldReturnCorrectUrl() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.UPCOMING_RELEASES.getUrl());
    assertEquals(UrlType.UPCOMING_RELEASES.getUrl(), 
        connector.connect().getURL(), "URL did not match");
  }
  
  @Test
  void whenConnectShouldHaveTimeout() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl());
    final int timeout = connector.connect().getReadTimeout();
    assertEquals(5000, timeout, "Timeout did not match the expected value");
  }

  @Test
  void whenConnectShouldHaveLanguageRequestProperty() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl());
    final String language = connector.connect().getRequestProperty("Accept-Language");
    assertEquals("en-US,en;q=0.8", language, "Language did not match the expected value");
  }

  @Test
  void whenConnectShouldHaveUserAgentRequestProperty() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl());
    final String agent = connector.connect().getRequestProperty("User-Agent");
    assertEquals(USER_AGENT, agent, "User agent did not match the expected value");
  }
  
  @Test
  void whenConnectShouldHaveUserRefererProperty() throws IOException {
    final String referer = connector.connect().getRequestProperty("Referer");
    assertEquals("https://www.google.com/", referer, "Referer did not match the expected value");
  }
  
}
