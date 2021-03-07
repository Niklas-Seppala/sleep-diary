package com.example.sleepdiary;

import android.os.Build;
import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.GlobalData;

public class StatsActivity extends AppCompatActivity {
    private final ChartFragment chartFrag = new ChartFragment();
    private final ListFragment listFrag = new ListFragment();

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            if (GlobalData.getInstance().getUserModels().size() == 0)
                GlobalData.__DEV__populateDb(db, "nikke", 7.5, 1613757955, 60);
            db.close();
        }

        setToggleClicks();
    }

    /**
     * Set click eventhandler to list/chart toggle button.
     */
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

    /**
     * Set currently displayed fragment.
     * @param fragment Displayed fragment (ListFragment / ChartFragment)
     */
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.statsFrameLayout, fragment)
                .commit();
    }
}
