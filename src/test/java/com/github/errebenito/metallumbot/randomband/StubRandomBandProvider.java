package com.github.errebenito.metallumbot.randomband;

class StubRandomBandProvider implements RandomBandProvider {

    private final String url;

    StubRandomBandProvider(String url) {
        this.url = url;
    }

    @Override
    public String getRandomBandUrl() {
        return url;
    }
}
