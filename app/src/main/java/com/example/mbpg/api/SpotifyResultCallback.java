package com.example.mbpg.api;

public interface SpotifyResultCallback {
    void onSuccess(String trackUri, String trackName, String artistName, String albumArtUrl, String previewUrl, boolean isPlayable);
    void onError(String message);
}
