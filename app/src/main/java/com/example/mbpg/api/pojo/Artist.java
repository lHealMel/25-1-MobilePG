package com.example.mbpg.api.pojo;

import com.google.gson.annotations.SerializedName;

public class Artist {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
