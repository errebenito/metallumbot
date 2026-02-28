package com.github.errebenito.metallumbot.upcomingalbum;

public record Album(String bandUrl, String bandName, String albumUrl, String albumName, String type, String genre) {
    @Override
    public String toString  () {
        return String.format("%s – %s%n%s%n%s%n%s", this.bandName(), this.albumName(), this.albumUrl(), this.type(),this.genre());
    }
}
