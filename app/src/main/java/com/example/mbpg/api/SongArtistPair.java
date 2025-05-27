package com.example.mbpg.api;

public class SongArtistPair {
    public final String song;
    public final String artistDisplayName;
    public final String artistSearchName; // 새로 추가

    public SongArtistPair(String song, String artistDisplayName, String artistSearchName) {
        this.song = song;
        this.artistDisplayName = artistDisplayName;
        this.artistSearchName = artistSearchName;
    }

    public String getSong() {
        return song;
    }

    public String getArtistDisplayName() {
        return artistDisplayName;
    }

    public String getArtistSearchName() {
        return artistSearchName;
    }
}
