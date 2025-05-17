package com.github.errebenito.metallumbot.command;

import com.github.errebenito.metallumbot.connector.UrlConnector;
import java.net.MalformedURLException;
import java.net.URL;

public class CommandRunnerFactoryImpl implements CommandRunnerFactory {
    @Override
    public CommandRunner create(URL url) throws MalformedURLException {
        UrlConnector connector = new UrlConnector().withUrl(url);
        return new CommandRunner(connector);
    }
}
