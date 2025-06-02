package com.example.mbpg;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        EditText mbtiInput = findViewById(R.id.mbti_input);
        EditText moodInput = findViewById(R.id.mood_input);
        TextView selectedDateText = findViewById(R.id.selected_date_text);
        Button saveButton = findViewById(R.id.save_button);
        Button clearButton = findViewById(R.id.clear_button);

        final String[] selectedDate = {""};

        // 날짜 선택 리스너
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate[0] = year + "-" + (month + 1) + "-" + dayOfMonth;
            selectedDateText.setText("선택한 날짜: " + selectedDate[0]);

            // 🔍 해당 날짜에 저장된 기록 불러오기
            String key = "record_" + selectedDate[0];
            String saved = getSharedPreferences("mbti_records", MODE_PRIVATE)
                    .getString(key, null);

            if (saved != null) {
                Toast.makeText(this, "기록 있음: " + saved, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이 날짜에 저장된 기록이 없습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // 저장 버튼 클릭 시 기록 저장
        saveButton.setOnClickListener(view -> {
            String mbti = mbtiInput.getText().toString().trim();
            String mood = moodInput.getText().toString().trim();

            if (selectedDate[0].isEmpty()) {
                Toast.makeText(this, "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mbti.isEmpty() || mood.isEmpty()) {
                Toast.makeText(this, "MBTI와 기분을 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            String key = "record_" + selectedDate[0];
            String value = "MBTI: " + mbti + ", 기분: " + mood;

            getSharedPreferences("mbti_records", MODE_PRIVATE)
                    .edit()
                    .putString(key, value)
                    .apply();

            Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show();
        });

        // 초기화 버튼 클릭 시 전체 기록 삭제
        clearButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("기록 초기화")
                    .setMessage("모든 기록을 삭제하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        getSharedPreferences("mbti_records", MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();
                        Toast.makeText(this, "기록이 모두 초기화되었습니다", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        });
    }
}
