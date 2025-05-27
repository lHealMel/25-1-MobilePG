package com.example.mbpg.data;

import android.util.Log;

import com.example.mbpg.model.MbtiResult;
import com.example.mbpg.util.ApiRequest;
import com.example.mbpg.util.ApiResponse;
import com.example.mbpg.util.GeminiApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DummyMusicData {

    public static void getResultByMbti(String mbti, MbtiResultCallback callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // In emulator, localhost : 10.0.2.2
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeminiApi service = retrofit.create(GeminiApi.class);
        ApiRequest request = new ApiRequest(mbti);

        service.askGemini(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse data = response.body();
                    String genre = data.getGenre();
                    String genre_reason = ""; // can get the genre reason from API Response. Not used yet.

                    StringBuilder songsBuilder = new StringBuilder();
                    for (Map.Entry<String, String> entry : data.getSongs().entrySet()) {
                        songsBuilder.append("• ")
                                .append(entry.getKey())
                                .append(" - ")
                                .append(entry.getValue())
                                .append("\n");
                    }
                    MbtiResult result = new MbtiResult(mbti, genre, songsBuilder.toString());
                    callback.onResultReady(result);
                } else {
                    Log.e("Error", "Response failed with code: " + response.code());
                    callback.onResultReady(new MbtiResult(mbti, "Unknown", "No songs"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
                callback.onResultReady(new MbtiResult(mbti, "Error", "Failed to load"));
            }
        });
    }
}
