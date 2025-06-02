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

import com.example.mbpg.data.QuestionFactory;
import com.example.mbpg.model.Question;
import com.example.mbpg.util.MbtiCalculator;

import java.util.List;

public class MbtiTestActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentIndex = 0;
    private MbtiCalculator calculator;
    private TextView questionText;
    private TextView progressText;
    private Button optionA, optionB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mbti_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.test), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressText = findViewById(R.id.progress_text);
        questionText = findViewById(R.id.question_text);
        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);

        calculator = new MbtiCalculator();
        questions = QuestionFactory.create();

        showQuestion();

        optionA.setOnClickListener(v -> handleAnswer(true));

        optionB.setOnClickListener(v -> handleAnswer(false));
    }

    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            questionText.setText(q.getQuestion());
            optionA.setText(q.getOptionA());
            optionB.setText(q.getOptionB());

            String progress = (currentIndex + 1) + " / " + questions.size();
            progressText.setText(progress);
        }
    }

    private void handleAnswer(boolean choseA) {
        Question q = questions.get(currentIndex);
        calculator.addTrait(q.getTrait(), choseA);
        currentIndex++;

        if (currentIndex < questions.size()) {
            showQuestion();
        } else {
            String result = calculator.getResult();
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("MBTI_RESULT", result);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }
}
