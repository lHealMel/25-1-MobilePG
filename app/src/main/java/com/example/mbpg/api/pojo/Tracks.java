package com.example.mbpg.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tracks {
    @SerializedName("items")
    private List<TrackItem> items;

    @SerializedName("total")
    private int total;

    // getters and setters
    public List<TrackItem> getItems() {
        return items;
    }

    public void setItems(List<TrackItem> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
