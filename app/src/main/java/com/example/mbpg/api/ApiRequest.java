package com.example.mbpg.api;

public class ApiRequest {
    private String mbti;
    private String artist;
    private String song;

    public ApiRequest(String mbti) {
        this.mbti = mbti;
    }

    public ApiRequest(String artist, String songName) {
        this.artist = artist;
        this.song = songName;
    }

    public String getMbti() {
        return mbti;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setSong(String song) {
        this.song = song;
    }
}