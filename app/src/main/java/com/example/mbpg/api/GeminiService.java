package com.example.mbpg.api;

import android.util.Log;

import com.example.mbpg.api.pojo.ArtistInfoPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GeminiService {

    public static void getResultByMbti(String mbti, MbtiResultCallback callback) {

        // Gemeni API의 응답 시간이 늘어남에 따라, timeout이 발생합니다.
        // 때문에, OkHttp를 사용해 timeout 시간을 20초로 넉넉하게 설정합니다.
        // Retrofit은 OkHttp기반 통신이기에, OkHttp로 설정이 가능합니다.
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        ApiRequest request = new ApiRequest(mbti);

        service.askGemini(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse data = response.body();
                    String genre = data.getGenre();
                    // String genre_reason = ""; // can get the genre reason from API Response. Not used yet.

                    List<SongArtistPair> songArtistPairs = new ArrayList<>();
                    if (data.getSongs() != null) { //
                        for (Map.Entry<String, ArtistInfoPojo> entry : data.getSongs().entrySet()) { //
                            String songTitle = entry.getKey();
                            ArtistInfoPojo artistInfo = entry.getValue();
                            if (artistInfo != null) {
                                songArtistPairs.add(new SongArtistPair(
                                        songTitle,
                                        artistInfo.getDisplayName(),
                                        artistInfo.getSearchName()
                                ));
                            }
                        }
                    }

                    MbtiResult result;
                    if (!songArtistPairs.isEmpty()) {
                        result = new MbtiResult(mbti, genre, songArtistPairs);
                    } else {
                        result = new MbtiResult(mbti, genre, "추천 음악을 찾지 못했습니다.", "");
                    }
                    callback.onResultReady(result); //
                } else {
                    Log.e("Error", "Response failed with code: " + response.code());
                    callback.onResultReady(new MbtiResult(mbti, "Error", "Response failed with code: " + response.code(), "")); //
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
                callback.onResultReady(new MbtiResult(mbti, "Error", "Failed to load", ""));
            }
        });
    }

}
