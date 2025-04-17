package com.example.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewActivity extends AppCompatActivity {

    String department;
    String name;
    int student_number;
    Button returnbtn;
    EditText new_url;
    EditText new_phone;
    String tag = "nLifeCycle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_new), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(tag, "In the onCreate() event");

        Intent myCallerIntent = getIntent();
        if (myCallerIntent != null && myCallerIntent.hasExtra("Department")
                && myCallerIntent.hasExtra("Name")
                && myCallerIntent.hasExtra("StudentNumber")) {

            department = myCallerIntent.getStringExtra("Department");
            name = myCallerIntent.getStringExtra("Name");
            student_number = myCallerIntent.getIntExtra("StudentNumber", -1); // default 값 설정
        } else {
            Toast.makeText(this, "Invalid Data.", Toast.LENGTH_SHORT).show();
            finish(); // Just Finish NewActivity
        }

        new_phone = findViewById(R.id.modifyPhone);
        new_url = findViewById(R.id.modifyUrl);


        Toast.makeText(this, "Student Info : " + name + ", " + department + ", " + student_number, Toast.LENGTH_SHORT).show();

        returnbtn = findViewById(R.id.returnbtn);
        returnbtn.setOnClickListener(v -> {
            Intent newIntent = new Intent();
            Bundle newBundle = new Bundle();
            newBundle.putString("newPhone", new_phone.getText().toString());
            newBundle.putString("newUrl", new_url.getText().toString());
            newIntent.putExtras(newBundle);
            setResult(Activity.RESULT_OK, newIntent);
            finish();
        });

    }

    public void onStart() {
        super.onStart();
        Log.d(tag, "In the onStart() event");
    }

    public void onRestart() {
        super.onRestart();
        Log.d(tag, "In the onRestart() event");
    }

    public void onResume() {
        super.onResume();
        Log.d(tag, "In the onResume() event");
    }

    public void onPause() {
        super.onPause();
        Log.d(tag, "In the onPause() event");
    }

    public void onStop() {
        super.onStop();
        Log.d(tag, "In the onStop() event");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(tag, "In the onDestroy() event");
    }

}