package com.example.mbpg.api.pojo;

import com.google.gson.annotations.SerializedName;

public class ArtistInfoPojo {
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("search_name")
    private String searchName;

    public String getDisplayName() {
        return displayName;
    }

    public String getSearchName() {
        return searchName;
    }
}