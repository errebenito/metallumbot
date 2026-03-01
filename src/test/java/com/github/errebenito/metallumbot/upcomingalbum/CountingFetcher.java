package com.github.errebenito.metallumbot.upcomingalbum;

public class CountingFetcher implements UpcomingAlbumDataFetcher {
        int calls = 0;
        private final String json;

        CountingFetcher(String json) {
            this.json = json;
        }

        @Override
        public String fetch() {
            calls++;
            return json;
        }
    }
