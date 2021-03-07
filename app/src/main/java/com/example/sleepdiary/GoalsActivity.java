package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GoalsActivity extends AppCompatActivity {
    private Button submitBtn;
    private TextView hoursTextView;
    private TextView minsTextView;
    private TextView caffeineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        findViews();
        setSubmitClickHander();
    }

    private void findViews() {
        submitBtn = findViewById(R.id.goals_submit_btn);
        hoursTextView = findViewById(R.id.goals_hours_input);
        minsTextView = findViewById(R.id.goals_mins_input);
        caffeineTextView = findViewById(R.id.goals_caffeine_input);
    }

    private void setSubmitClickHander() {
        submitBtn.setOnClickListener(v -> {
            String hours = hoursTextView.getText().toString();
            String minutes = minsTextView.getText().toString();
            String cup = caffeineTextView.getText().toString();
        });
    }
}