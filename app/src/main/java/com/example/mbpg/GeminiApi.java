package com.example.mbpg;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GeminiApi {
    @POST("/ask")
    Call<ApiResponse> askGemini(@Body ApiRequest request);
}