package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GoalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Button buttonResult = findViewById(R.id.submit_results);
        buttonResult.setOnClickListener(v -> {
            TextView hoursResult = findViewById(R.id.h);
            String hours = hoursResult.getText().toString();
            TextView minutesResult = findViewById(R.id.min);
            String minutes = minutesResult.getText().toString();
            TextView caffeineResult = findViewById(R.id.cups);
            String cup = caffeineResult.getText().toString();
        });
    }
}