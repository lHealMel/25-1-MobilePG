package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mbpg.data.DummyMusicData;


public class ResultActivity extends AppCompatActivity {

    private TextView resultText, genreText, songsText;
    private Button btnBackToHome;

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
        btnBackToHome = findViewById(R.id.btn_back_to_home); //  버튼 연결

        String mbti = getIntent().getStringExtra("MBTI_RESULT");
        resultText.setText(getString(R.string.mbti_type , mbti));


        DummyMusicData.getResultByMbti(mbti, result -> runOnUiThread(()->{
            genreText.setText(getString(R.string.mbti_genre, result.getGenres()));
            songsText.setText(getString(R.string.mbti_songs, result.getSongs()));
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
