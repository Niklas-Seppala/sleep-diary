package com.example.sleepdiary;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;


/**
 * Home view of the whole application.
 * From here, user can view stats, start new alarm, start new sleep entry
 * and set personal goals.
 */
public class HomeFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHomeViewButtonEvents(view);
    }

    /**
     * Set the click eventhandlers to main feature buttons
     * @param view HomeFragment
     */
    private void setHomeViewButtonEvents(View view) {
        // Statistics Button Click
        view.findViewById(R.id.btnStats).setOnClickListener(v -> {
            Intent stats = new Intent(v.getContext(), StatsActivity.class);
            startActivity(stats);
        });
        // Sleep Button Click
        view.findViewById(R.id.btnSleep).setOnClickListener(v -> {
            StartSleepEventFragment fragment = new StartSleepEventFragment();
            fragment.show(getFragmentManager(), "gotosleep");
        });
        // Goal Button Click
        view.findViewById(R.id.btnGoal).setOnClickListener(v -> {
            Intent goal = new Intent(v.getContext(), GoalsActivity.class);
            startActivity(goal);
        });
        // Alarm Buttom Click
        view.findViewById(R.id.btnAlarm).setOnClickListener(v ->  {
            Intent alarm = new Intent(v.getContext(), AlarmActivity.class);
            startActivity(alarm);
        });
    }

    // Dont touch!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}