package com.github.errebenito.metallumbot.randomband;

class ThrowingRandomBandProvider implements RandomBandProvider {

    @Override
    public String getRandomBandUrl() {
        throw new RuntimeException("boom");
    }
}
