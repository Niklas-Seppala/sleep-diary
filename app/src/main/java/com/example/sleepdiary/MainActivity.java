package com.example.sleepdiary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.SleepEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Default activity that opens at the start of the app
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    public final SettingsFragment settingsFrag = new SettingsFragment();
    public final HomeFragment homeFrag = new HomeFragment();
    public final InfoFragment infoFrag = new InfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDataFromDB();
        AppSettings.read(getSharedPreferences(AppSettings.NAME, Context.MODE_PRIVATE));
        initBottomNavBar();
        setFragment(this.homeFrag);
        handlePartialEntry();
    }

    /**
     * Initialize bottom navigation bar.
     */
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

    /**
     * Load data from database to Global data.
     */
    private void loadDataFromDB() {
        if (GlobalData.isDirty()) {
            DbConnection db = new DbConnection(this);
            GlobalData.update(db);
            db.close();
        }
    }

    /**
     * Check if the SleepEntry "stack" has partial entry on top.
     * If so, launch WakeUpEventFragment dialog, that requests user
     * to finish the entry.
     */
    private void handlePartialEntry() {
        List<SleepEntry> partialEntries = GlobalData.getInstance().getPartialSleepEntries();
        if (partialEntries.isEmpty())
            return;

        SleepEntry latest = partialEntries.get(0);
        if (latest.isIncomplete()) {
            WakeUpEventFragment fragment = new WakeUpEventFragment();
            fragment.show(getSupportFragmentManager(), "wakeup");
        }
    }

    /**
     * Switch between home/settings/tips fragments.
     * @param fragment Fragment you want to set.
     */
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fl_fragment, fragment)
            .commit();
    }
}