package com.github.errebenito.metallumbot.command;

import java.net.MalformedURLException;
import java.net.URL;

public interface CommandRunnerFactory {
    CommandRunner create(URL url) throws MalformedURLException;
}
