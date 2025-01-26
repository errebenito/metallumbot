package com.github.errebenito.metallumbot.command;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;


/**
 * Unit tests for the CommandRunner class.

 * @author rbenito
 *
 */
@ExtendWith(EasyMockExtension.class)
class CommandRunnerTest {
  
  @BeforeAll
  static void setUp() {
    System.setProperty("https.protocols", "TLSv1.2");
  }
  
  @Test
  void whenDoBandShouldReturnBandLink() throws MalformedURLException {
    final CommandRunner runner = new CommandRunner(new UrlConnector()
        .withUrl(UrlType.RANDOM_BAND.getUrl()));
    final String result = runner.doBand();
    assertTrue(result.contains("https://www.metal-archives.com/band/view/id/"), "Return value was not a band link: " + result);
  }

  @Test
  void whenDoUpcomingShouldReturnAlbumLink() throws MalformedURLException {
    final CommandRunner runner = new CommandRunner(new UrlConnector()
        .withUrl(UrlType.UPCOMING_RELEASES.getUrl()));
    final String result = runner.doUpcoming();
    assertTrue(result.contains("https://www.metal-archives.com/albums/"), "Return value did not contain an album link: " + result);
  }  
  
  @Test
  void whenConnectorThrowsDoUpcomingShouldCatchException() {
    final UrlConnector connector = createMock(UrlConnector.class);

    try {
      expect(connector.readUpcomingAlbumsJson()).andThrow(new IOException());
    } catch (IOException e) {
      // Intentionally empty
    }
    
    replay(connector);
    
    final CommandRunner runner = new CommandRunner(connector);
    runner.doUpcoming();
    
    verify(connector);
  }
  
  @Test
  void whenConnectorThrowsDoBandShouldCatchException() {
    final UrlConnector connector = createMock(UrlConnector.class);
    
    try {
      connector.connect();
      expectLastCall().andStubThrow(new IOException());
    } catch (IOException e) {
      fail("Mock setup should not throw exception");
    }
    replay(connector);
    
    final CommandRunner runner = new CommandRunner(connector);
    runner.doBand();
    
    verify(connector);
  }
}
