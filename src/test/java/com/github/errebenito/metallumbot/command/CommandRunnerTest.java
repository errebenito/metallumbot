package com.github.errebenito.metallumbot.command;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;
import java.io.IOException;
import java.net.MalformedURLException;
import org.easymock.EasyMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the CommandRunner class.

 * @author rbenito
 *
 */
@ExtendWith(EasyMockExtension.class)
class CommandRunnerTest {
     
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
  void whenDoBandWithBadUrlShouldThrowException() throws IOException {
    final UrlConnector mockConnector = createMock(UrlConnector.class);
    expect(mockConnector.connect()).andThrow(new IOException());
    replay(mockConnector);
    final CommandRunner runner = new CommandRunner(mockConnector);
    runner.doBand();
    verify(mockConnector);  
  }
  
  @Test
  void whenDoUpcomingWithBadUrlShouldThrowException() throws IOException {
    final UrlConnector mockConnector = createNiceMock(UrlConnector.class);
    expect(mockConnector.connect()).andThrow(new IOException());
    replay(mockConnector);
    final CommandRunner runner = new CommandRunner(mockConnector);
    runner.doBand();
    verify(mockConnector);  
  }
  
}
