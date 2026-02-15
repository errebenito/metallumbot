package com.github.errebenito.metallumbot.upcomingalbum;

public class CurlProcessStarter implements ProcessStarter {
    @Override
    public Process start(String url) throws Exception {
        return new ProcessBuilder("curl", "-s", url)
                .redirectErrorStream(true)
                .start();
    }
}

