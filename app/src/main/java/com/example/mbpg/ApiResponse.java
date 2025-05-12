package com.example.mbpg;

import java.util.Map;

public class ApiResponse {
    private String genre;
    private String genre_reason;
    private Map<String, String> songs;

    /*
        Usage
        From json-like Response from API,
        get Genre, Genre_reason, Songs

        ex :
        At onResponse,
        ApiResponse data = response.body();
        data.get[Genre, Genre_reason, Songs]();

    */
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre_reason() {
        return genre_reason;
    }

    public void setGenre_reason(String genre_reason) {
        this.genre_reason = genre_reason;
    }

    public Map<String, String> getSongs() {
        return songs;
    }

    public void setSongs(Map<String, String> songs) {
        this.songs = songs;
    }
}
