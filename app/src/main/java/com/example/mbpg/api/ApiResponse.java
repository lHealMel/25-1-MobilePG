package com.example.mbpg.api;

import com.example.mbpg.api.pojo.ArtistInfoPojo;
import com.example.mbpg.api.pojo.Tracks;

import java.util.Map;

public class ApiResponse {
    private String genre;
    private String genre_reason;
    private Map<String, ArtistInfoPojo> songs;
    private Tracks tracks;

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

    public String getGenre_reason() {
        return genre_reason;
    }

    public Map<String, ArtistInfoPojo> getSongs() {
        return songs;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    public void setGenre_reason(String genre_reason) {
        this.genre_reason = genre_reason;
    }


    public void setSongs(Map<String, ArtistInfoPojo> songs) {
        this.songs = songs;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }
}
