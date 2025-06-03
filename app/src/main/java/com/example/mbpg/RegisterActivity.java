package com.example.mbpg;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.reg_input_email);
        passwordInput = findViewById(R.id.reg_input_password);
        Button registerButton = findViewById(R.id.reg_submit_btn);

        registerButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase New user registration
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.d("RegisterActivity", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}