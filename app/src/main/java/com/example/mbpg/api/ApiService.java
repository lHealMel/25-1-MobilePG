package com.example.mbpg.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/MBTI")
    Call<ApiResponse> askGemini(@Body ApiRequest request);

    @POST("/search")
    Call<ApiResponse> song_search(@Body ApiRequest request);
}