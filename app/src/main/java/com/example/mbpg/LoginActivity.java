package com.example.mbpg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText emailInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        Button loginButton = findViewById(R.id.btn_login);
        Button registerButton = findViewById(R.id.btn_reg);
        emailInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);

        credentialManager = CredentialManager.create(this);


        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.btn_google_login).setOnClickListener(v -> {
            signInWithGoogle();
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });


    }

    private void signInWithGoogle() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false).setServerClientId(getString(R.string.default_web_client_id)).build();

        GetCredentialRequest request = new GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build();

        credentialManager.getCredentialAsync(this, request, null, ContextCompat.getMainExecutor(this), new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
            @Override
            public void onResult(GetCredentialResponse result) {
                Credential credential = result.getCredential();
                GoogleIdTokenCredential googleIdTokenCredential = null;

                if (credential instanceof CustomCredential) {
                    CustomCredential customCredential = (CustomCredential) credential;
                    if (customCredential.getType().equals(GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                        googleIdTokenCredential = GoogleIdTokenCredential.createFrom(customCredential.getData());
                    }
                } else if (credential instanceof GoogleIdTokenCredential) {
                    googleIdTokenCredential = (GoogleIdTokenCredential) credential;
                }


                if (googleIdTokenCredential != null) {
                    String idToken = googleIdTokenCredential.getIdToken();
                    Log.d(TAG, "Successfully obtained Google ID token.");
                    firebaseAuthWithGoogle(idToken);
                } else {
                    Log.e(TAG, "Error: The returned credential is not a recognizable Google credential. Type: " + credential.getClass().getName());
                    Toast.makeText(LoginActivity.this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(GetCredentialException e) {
                Log.e(TAG, "GetCredentialException", e);
                Toast.makeText(LoginActivity.this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Google 로그인 성공!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Firebase 인증에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}