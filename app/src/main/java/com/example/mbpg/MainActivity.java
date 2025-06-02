package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button startTestButton = findViewById(R.id.btn_start_test);
        Button openCalendarButton = findViewById(R.id.btn_open_calendar);
        Button logoutButton = findViewById(R.id.btn_logout);

        // 테스트시작 버튼
        startTestButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MbtiTestActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 기록보기 버튼
        openCalendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        // 로그아웃 버튼
        logoutButton.setOnClickListener(v -> logOut());
    }

    // 로그아웃 메서드
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "로그아웃 하셨습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
