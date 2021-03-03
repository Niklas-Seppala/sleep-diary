package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final SettingsFragment settingsFrag = new SettingsFragment();
    private final HomeFragment homeFrag = new HomeFragment();
    private final InfoFragment infoFrag = new InfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set bottom navigation bar click events
        initBottomNavBar();

        // Set default fragment
        setFragment(this.homeFrag);
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

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fl_fragment, fragment)
            .commit();
    }
}