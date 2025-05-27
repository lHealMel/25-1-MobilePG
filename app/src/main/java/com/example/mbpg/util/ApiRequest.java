package com.example.mbpg.util;

public class ApiRequest {
    private String mbti;

    public ApiRequest(String mbti) {
        this.mbti = mbti;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }
}
