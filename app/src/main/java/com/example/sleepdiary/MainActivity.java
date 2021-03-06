package com.example.sleepdiary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.sleepdiary.data.AppSettings;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.SleepEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final SettingsFragment settingsFrag = new SettingsFragment();
    public final HomeFragment homeFrag = new HomeFragment();
    public final InfoFragment infoFrag = new InfoFragment();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDataFromDB();
        AppSettings.deserialize(getSharedPreferences(AppSettings.NAME, Context.MODE_PRIVATE));

        // Set bottom navigation bar click events
        initBottomNavBar();

        // Set default fragment
        setFragment(this.homeFrag);

//        deleteDatabase("sleep.db"); // TODO: DEV

        handlePartialEntry();
    }

    private void initBottomNavBar() {
        final BottomNavigationView btmNav = findViewById(R.id.bottomNavigationView);

        // Set home as default selected item.
        btmNav.setSelectedItemId(R.id.menu_item_home);

        // Set naviagtion bar button clicks
        btmNav.setOnNavigationItemSelectedListener(item -> {
            final int itemId = item.getItemId();

            // Find corresponding fragment to pressed button
            if (item.getItemId() == R.id.menu_item_home)  setFragment(this.homeFrag);
            else if (itemId == R.id.menu_item_info)  setFragment(this.infoFrag);
            else if (itemId == R.id.menu_item_settings) setFragment(this.settingsFrag);

            // Set selected visuals
            return true;
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadDataFromDB() {
        if (GlobalData.isDirty()) {
            DbConnection db = new DbConnection(this);
            GlobalData.update(db);
            db.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handlePartialEntry() {
        List<SleepEntry> entries = GlobalData.getInstance().getSleepEntries();
        if (entries.isEmpty())
            return;

        SleepEntry latest = entries.get(0);
        if (latest.isIncomplete()) {
            WakeUpEventFragment fragment = new WakeUpEventFragment();
            fragment.show(getSupportFragmentManager(), "wakeup");
        }
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fl_fragment, fragment)
            .commit();
    }
}