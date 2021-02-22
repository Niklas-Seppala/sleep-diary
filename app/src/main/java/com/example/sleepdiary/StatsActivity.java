package com.example.sleepdiary;

import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.data.DbConnection;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.SleepModel;
import com.example.sleepdiary.data.UserModel;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private final ChartFragment chartFrag = new ChartFragment();
    private final ListFragment listFrag = new ListFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Set list as a default fragment
        setFragment(this.listFrag);

        // Set global data if necessary
        if (GlobalData.isDirty()) {
            DbConnection db = new DbConnection(this);
            GlobalData.update(db);

            // TODO: DEV
            if (GlobalData.getInstance().getUserModels().size() == 0) {
                GlobalData.populateMockData(db);
            }

            db.close();
        }
        // Set click handlers
        setToggleClicks();
    }

    private void setToggleClicks() {
        ToggleButton toggle = findViewById(R.id.statsToggleBtn);
        toggle.setChecked(true);
        toggle.setOnClickListener(view -> {
            if (toggle.isChecked())
                setFragment(this.listFrag);
            else
                setFragment(this.chartFrag);
        });
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.statsFrameLayout, fragment)
                .commit();
    }
}
