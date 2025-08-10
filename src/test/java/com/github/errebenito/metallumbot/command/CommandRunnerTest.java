package com.github.errebenito.metallumbot.command;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;


/**
 * Unit tests for the CommandRunner class.

 * @author rbenito
 *
 */
@ExtendWith(MockitoExtension.class)
class CommandRunnerTest {
  @Mock
  UrlConnector connector;

  @InjectMocks
  CommandRunner runner;

  
  @BeforeAll
  static void setUp() {
    System.setProperty("https.protocols", "TLSv1.2");
  }
  
  @Test
  void whenDoBandShouldReturnBandLink() throws MalformedURLException {
    runner = new CommandRunner(new UrlConnector()
        .withUrl(UrlType.RANDOM_BAND.getUrl()));
    final String result = runner.doBand();
    assertTrue(result.contains("https://www.metal-archives.com/band/view/id/"), "Return value was not a band link: " + result);
  }

  @Test
  void whenDoUpcomingShouldReturnAlbumLink() throws MalformedURLException {
    URL url = new File("src/test/resources/upcoming_albums_test_data.json").toURI().toURL();
    runner = new CommandRunner(new UrlConnector()
        .withUrl(url));
    final String result = runner.doUpcoming();
    assertTrue(result.contains("https://www.metal-archives.com/albums/"), "Return value did not contain an album link: " + result);
  }  
  
  @Test
  void whenConnectorThrowsDoUpcomingShouldCatchException() throws IOException {
    when(connector.readUpcomingAlbumsJson()).thenThrow(new IOException());

    runner = new CommandRunner(connector);
    runner.doUpcoming();

    verify(connector).readUpcomingAlbumsJson();
  }
  
  @Test
  void whenConnectorThrowsDoBandShouldCatchException() throws IOException {
    doThrow(new IOException()).when(connector).connect();

    runner = new CommandRunner(connector);
    runner.doBand();

    verify(connector).connect();
  }
}
