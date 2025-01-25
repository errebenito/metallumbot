package com.github.errebenito.metallumbot.connector;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Enum for the different URL types that the UrlConnector can access.

 * @author rbenito
 *
 */
public enum UrlType {
  RANDOM_BAND("https://www.metal-archives.com/band/random"),
  UPCOMING_RELEASES("https://www.metal-archives.com/release/ajax-upcoming/json");

  private final String url;

  UrlType(final String url) {
    this.url = url;
  }
  
  public URL getUrl() throws MalformedURLException {
    return  URI.create(this.url).toURL();
  }
}
