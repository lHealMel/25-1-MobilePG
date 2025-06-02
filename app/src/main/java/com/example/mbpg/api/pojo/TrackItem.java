package com.example.mbpg.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackItem {
    @SerializedName("album")
    private Album album;

    @SerializedName("artists")
    private List<Artist> artists;

    @SerializedName("name")
    private String name; // 트랙 이름

    @SerializedName("uri")
    private String uri; // Spotify 트랙 URI (재생 시 필요)

    @SerializedName("preview_url")
    private String previewUrl; // 30초 미리듣기 URL

    @SerializedName("is_playable")
    private boolean isPlayable; // 재생 가능 여부

    @SerializedName("id")
    private String id; // 트랙 ID

    @SerializedName("popularity")
    private int popularity;


    // getters and setters
    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
