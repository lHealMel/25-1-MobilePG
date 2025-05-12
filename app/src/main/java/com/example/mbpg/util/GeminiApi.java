package com.example.mbpg.util;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GeminiApi {
    @POST("/MBTI")
    Call<ApiResponse> askGemini(@Body ApiRequest request);
}