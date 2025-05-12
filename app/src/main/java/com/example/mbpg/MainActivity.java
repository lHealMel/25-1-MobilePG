package com.example.mbpg;
import java.util.Map;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView text1;

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

        text1 = findViewById(R.id.text1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000") // In emulator, use 10.0.2.2, not localhost
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeminiApi service = retrofit.create(GeminiApi.class);
        ApiRequest request = new ApiRequest("INFP");

        service.askGemini(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse data = response.body();

                    String displayText = "Genre: " + data.getGenre() + "\n"
                            + "Reason: " + data.getGenre_reason() + "\n"
                            + "Songs:\n";

                    for (Map.Entry<String, String> entry : data.getSongs().entrySet()) {
                        displayText += "- " + entry.getKey() + " by " + entry.getValue() + "\n";
                    }

                    text1.setText(displayText);
                    Log.d("response", displayText);
                } else {
                    text1.setText("Invalid response format");
                    Log.e("Error", "Response failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Failure", t.getMessage());
            }
        });
    }


}