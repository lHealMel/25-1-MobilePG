package com.example.mbpg.api;

import android.util.Log;

import com.example.mbpg.api.pojo.TrackItem;
import com.example.mbpg.api.pojo.Tracks;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyService {

    private static final String TAG = "SpotifyService";

    public static void geturifrommbti(String artist, String song, SpotifyResultCallback callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // In emulator, localhost : 10.0.2.2
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        ApiRequest request = new ApiRequest(artist, song);


        service.song_search(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Tracks tracksObj = apiResponse.getTracks();

                    if (tracksObj != null && tracksObj.getItems() != null && !tracksObj.getItems().isEmpty()) {
                        // 여러 트랙 중 어떤 것을 선택할지 결정해야 합니다.
                        // 여기서는 첫 번째 트랙을 사용하거나, popularity가 가장 높은 트랙을 선택할 수 있습니다.
                        // 혹은 isPlayable이 true인 첫 번째 트랙을 찾을 수도 있습니다.
                        // 이 값들을 확인하여 재생 가능한 트랙을 우선적으로 선택해야 합니다.

                        TrackItem selectedTrack = null;

                        // 1. isPlayable이 true인 첫 번째 트랙 찾기 시도
                        for (TrackItem item : tracksObj.getItems()) {
                            if (item.isPlayable()) {
                                selectedTrack = item;
                                Log.d(TAG, "Found playable track: " + item.getName());
                                break;
                            }
                        }

                        // 2. playable 트랙이 없다면, 첫 번째 트랙이라도 사용 (또는 popularity 기준)
                        if (selectedTrack == null) {
                            selectedTrack = tracksObj.getItems().get(0);
                            Log.d(TAG, "No playable track found, using first track: " + selectedTrack.getName() + " (isPlayable: " + selectedTrack.isPlayable() + ")");
                        }
                        // isPlayable이 false인 경우, Spotify SDK로 재생이 안됩니다.


                        String trackUri = selectedTrack.getUri();
                        String trackName = selectedTrack.getName();
                        String previewUrl = selectedTrack.getPreviewUrl(); // null일 수 있음
                        boolean isPlayable = selectedTrack.isPlayable();

                        String artistName = "Unknown Artist";
                        if (selectedTrack.getArtists() != null && !selectedTrack.getArtists().isEmpty()) {
                            artistName = selectedTrack.getArtists().get(0).getName();
                        }

                        // 앨범 아트의 첫번째 이미지 선택
                        String albumArtUrl = null;
                        if (selectedTrack.getAlbum() != null && selectedTrack.getAlbum().getImages() != null && !selectedTrack.getAlbum().getImages().isEmpty()) {
                            albumArtUrl = selectedTrack.getAlbum().getImages().get(0).getUrl();
                        }

                        Log.i(TAG, "Track URI: " + trackUri);
                        Log.i(TAG, "Track Name: " + trackName);
                        Log.i(TAG, "Artist Name: " + artistName);
                        Log.i(TAG, "Album Art URL: " + albumArtUrl);
                        Log.i(TAG, "Preview URL: " + previewUrl);
                        Log.i(TAG, "Is Playable: " + isPlayable);

                        callback.onSuccess(trackUri, trackName, artistName, albumArtUrl, previewUrl, isPlayable);

                    } else {
                        Log.w(TAG, "No tracks found in the response.");
                        callback.onError("No tracks found.");
                    }
                } else {
                    Log.e(TAG, "API request failed. Code: " + response.code() + ", Message: " + response.message());
                    try {
                        if (response.errorBody() != null) {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    callback.onError("API request failed: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "API call failed.", t);
                callback.onError("Network error or API call failure: " + t.getMessage());
            }
        });
    }
}
