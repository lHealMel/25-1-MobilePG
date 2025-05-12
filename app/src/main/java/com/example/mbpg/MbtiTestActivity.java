package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import gachon.termproject.mbtimusic.data.QuestionFactory;
import gachon.termproject.mbtimusic.model.Question;
import gachon.termproject.mbtimusic.util.MbtiCalculator;

public class MbtiTestActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentIndex = 0;
    private MbtiCalculator calculator;

    private TextView questionText;
    private Button optionA, optionB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbti_test);

        questionText = findViewById(R.id.question_text);
        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);

        calculator = new MbtiCalculator();
        questions = QuestionFactory.create();

        showQuestion();

        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(true);
            }
        });

        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(false);
            }
        });
    }

    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            questionText.setText(q.getQuestion());
            optionA.setText(q.getOptionA());
            optionB.setText(q.getOptionB());
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
