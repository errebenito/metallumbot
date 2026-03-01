package com.github.errebenito.metallumbot.randomband;

class InterruptingRandomBandProvider implements RandomBandProvider {

    @Override
    public String getRandomBandUrl() throws InterruptedException {
        throw new InterruptedException("interrupted");
    }
}