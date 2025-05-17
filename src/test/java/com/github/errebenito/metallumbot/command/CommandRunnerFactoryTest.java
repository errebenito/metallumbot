package com.github.errebenito.metallumbot.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class CommandRunnerFactoryTest {
  @Test
  void testCommandRunnerFactoryCreatesRunner() throws MalformedURLException {
    CommandRunnerFactory factory = new CommandRunnerFactoryImpl();
    URL url = URI.create("https://metal-archives.com").toURL();
    CommandRunner runner = factory.create(url);
    assertNotNull(runner);
  }
}
