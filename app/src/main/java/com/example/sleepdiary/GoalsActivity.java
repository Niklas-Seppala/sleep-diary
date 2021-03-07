package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.User;

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


            User currentUser = GlobalData.getInstance().getCurrentUser();
            User newUser = new User(currentUser.getId(), currentUser.getName(), 2, 2);
            DbConnection db = new DbConnection(this);
            String[] params = { Integer.toString(currentUser.getId()) };
            db.update(DbTables.user.TABLE_NAME, newUser, "where id=?", params);
            db.close();
        });
    }
}