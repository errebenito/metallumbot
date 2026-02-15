package com.github.errebenito.metallumbot.upcomingalbum;

import java.nio.charset.StandardCharsets;

public class SystemCurlExecutor implements Executor {
    private final ProcessStarter starter;

    public SystemCurlExecutor(ProcessStarter starter) {
        this.starter = starter;
    }

    @Override
    public String execute(String url) throws Exception {

        Process process = starter.start(url);

        String output = new String(
            process.getInputStream().readAllBytes(),
            StandardCharsets.UTF_8
        );

        int exit = process.waitFor();
        if (exit != 0) {
            throw new IllegalStateException("curl failed with exit code " + exit);
        }

        return output;
    }
}

