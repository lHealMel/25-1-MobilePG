package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mbpg.api.GeminiService;
import com.example.mbpg.api.SongArtistPair;
import com.example.mbpg.api.SpotifyResultCallback;
import com.example.mbpg.api.SpotifyService;

import java.util.List;


public class ResultActivity extends AppCompatActivity {

    private TextView resultText, genreText, songsText;
    private Button btnBackToHome;
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.result), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        resultText = findViewById(R.id.result_text);
        genreText = findViewById(R.id.genre_text);
        songsText = findViewById(R.id.songs_text);
        btnBackToHome = findViewById(R.id.btn_back_to_home);

        String mbti = getIntent().getStringExtra("MBTI_RESULT");
        if (mbti == null) {
            mbti = "N/A";
        }
        resultText.setText(getString(R.string.mbti_type, mbti));


        GeminiService.getResultByMbti(mbti, result -> runOnUiThread(() -> {
            genreText.setText(getString(R.string.mbti_genre, result.getGenres()));
            songsText.setText(getString(R.string.mbti_songs, result.getSongs().toString())); // getSongs(): returns formatted; "• Artist - Song"

            List<SongArtistPair> pairs = result.getSongArtistPairs();
            if (pairs != null && !pairs.isEmpty()) {
                for (SongArtistPair pair : pairs) {
                    String artistForDisplay = pair.getArtistDisplayName(); // Artist name for display (may Korean name, UI or log)
                    String artistForSearch = pair.getArtistSearchName();  // Artist name for search (may English name)
                    String song = pair.getSong();

                    // Ensure artist and song are not null or empty before calling Spotify
                    if (artistForSearch != null && !artistForSearch.isEmpty() && song != null && !song.isEmpty()) {
                        Log.d(TAG, "Calling SpotifyService for Artist: " + artistForSearch + ", Song: " + song);
                        SpotifyService.geturifrommbti(artistForSearch, song, new SpotifyResultCallback() { //
                            @Override
                            public void onSuccess(String trackUri, String trackName, String artistName, String albumArtUrl, String previewUrl, boolean isPlayable) {
                                Log.i(TAG, "Spotify Success for " + trackName + " - " + artistName + ": URI " + trackUri + ", Playable: " + isPlayable);
                                // we may update UI further here, ex) show album art, music control button
                            }
                            @Override
                            public void onError(String message) {
                                Log.e(TAG, "Spotify Error for " + song + " by search_artists" + artistForSearch + ": " + message);
                            }
                        });
                    } else {
                        Log.w(TAG, "Skipping Spotify call due to empty artist or song. Artist: [" + artistForSearch + "], Song: [" + song + "]");
                    }
                }
            } else {
                Log.d(TAG, "No song/artist pairs available to query Spotify.");
            }
        }));


        // Move to main screen
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });
    }
}
