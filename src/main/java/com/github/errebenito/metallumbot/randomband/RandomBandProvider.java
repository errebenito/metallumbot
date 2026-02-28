package com.github.errebenito.metallumbot.randomband;

import java.io.IOException;

public interface RandomBandProvider {
    String getRandomBandUrl() throws IllegalStateException, IOException, InterruptedException;
}
