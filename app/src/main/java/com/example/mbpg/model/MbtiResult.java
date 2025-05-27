package com.example.mbpg.model;

public class MbtiResult {
    private String mbti;
    private String genres;
    private String songs;

    public MbtiResult(String mbti, String genres, String songs) {
        this.mbti = mbti;
        this.genres = genres;
        this.songs = songs;
    }

    public String getMbti() {
        return mbti;
    }

    public String getGenres() {
        return genres;
    }

    public String getSongs() {
        return songs;
    }
}
