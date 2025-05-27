package com.example.mbpg.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album {
    @SerializedName("name")
    private String name;

    @SerializedName("images")
    private List<Image> images;

    @SerializedName("release_date")
    private String releaseDate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}