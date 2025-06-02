package com.example.mbpg.api;

import java.util.Collections;
import java.util.List;

public class MbtiResult {
    private String mbti;
    private String genres;
    private List<SongArtistPair> songArtistPairs;
    private String singleSongMessage;
    private String singleArtistMessage;

    // Constructor for a list of song-artist pairs
    public MbtiResult(String mbti, String genres, List<SongArtistPair> pairs) {
        this.mbti = mbti;
        this.genres = genres;
        this.songArtistPairs = pairs;
    }

    // Constructor for single message (e.g., errors or no songs)
    public MbtiResult(String mbti, String genres, String message, String artistMessage) {
        this.mbti = mbti;
        this.genres = genres;
        this.singleSongMessage = message;
        this.singleArtistMessage = artistMessage; // Usually empty or "N/A" for messages
        this.songArtistPairs = Collections.emptyList(); // Ensure list is not null
    }

    public String getMbti() {
        return mbti;
    }

    public String getGenres() {
        return genres;
    }

    // Generates the string: "• Artist1 - Song1 \n • Artist2 - Song2"
    public CharSequence getSongs() {
        if (songArtistPairs != null && !songArtistPairs.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (SongArtistPair pair : songArtistPairs) {
                if (!first) {
                    sb.append("\n");
                }
                // Display format: "• Artist - Song"
                sb.append("• ").append(pair.getArtistDisplayName()).append(" - ").append(pair.getSong());
                first = false;
            }
            return sb;
        } else if (singleSongMessage != null) {
            String artistDisplay = singleArtistMessage == null || singleArtistMessage.isEmpty() || singleArtistMessage.equals("N/A") ? "" : singleArtistMessage + " - ";
            return new StringBuilder("• ").append(artistDisplay).append(singleSongMessage);
        }
        return "No information.";
    }

    public List<SongArtistPair> getSongArtistPairs() {
        return songArtistPairs != null ? songArtistPairs : Collections.emptyList();
    }
}