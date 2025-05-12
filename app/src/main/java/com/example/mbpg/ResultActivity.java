package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mbpg.data.DummyMusicData;
import com.example.mbpg.model.MbtiResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private TextView resultText, genreText, songsText;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.result_text);
        genreText = findViewById(R.id.genre_text);
        songsText = findViewById(R.id.songs_text);
        btnBackToHome = findViewById(R.id.btn_back_to_home); //  버튼 연결

        String mbti = getIntent().getStringExtra("MBTI_RESULT");
        resultText.setText("당신의 MBTI: " + mbti);



        MbtiResult result = DummyMusicData.getResultByMbti(mbti);
        genreText.setText("추천 장르: " + result.getGenres());
        songsText.setText("추천 곡:\n" + result.getSongs());

        // 👉 버튼 클릭 시 메인화면으로 이동
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish(); // 현재 액티비티 종료
            }
        });
    }
}
