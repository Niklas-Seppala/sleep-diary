package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final HomeFragment homeFragment = new HomeFragment();
    private final InfoFragment infoFragment = new InfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set default fragment
        setFragment(homeFragment);

        // Set bottom navigation bar click events
        setNavbarClicks();
    }

    public void setNavbarClicks() {
        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);

        nav.setOnNavigationItemSelectedListener(item -> {
            final int itemId = item.getItemId();
            Fragment target = null;
            if (item.getItemId() == R.id.menu_item_home) {
                target = this.homeFragment;
            } else if (itemId == R.id.menu_item_info) {
                target = this.infoFragment;
            } else if (itemId == R.id.menu_item_settings) {
                target = this.settingsFragment;
            }
            setFragment(target);
            return true;
        });
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment, fragment)
                .commit();
    }
}