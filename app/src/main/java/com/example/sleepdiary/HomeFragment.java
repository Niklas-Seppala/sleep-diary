package com.example.sleepdiary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    // Use this :)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHomeViewButtonEvents(view);
    }

    // Dont touch!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void setHomeViewButtonEvents(View view) {
        // STATS BUTTON
        view.findViewById(R.id.btnStats).setOnClickListener(v -> {
            Log.d("BTN", "Stats button clicked");
            Intent stats = new Intent(v.getContext(), StatsActivity.class);
            startActivity(stats);
        });

        // SLEEP BUTTON
        view.findViewById(R.id.btnSleep).setOnClickListener(v -> {
            Log.d("BTN", "Sleep button clicked");
        });

        // GOAL BUTTON
        view.findViewById(R.id.btnGoal).setOnClickListener(v -> {
            Log.d("BTN", "Goal button clicked");
        });

        // ALARM BUTTOn
        view.findViewById(R.id.btnAlarm).setOnClickListener(v -> {
            Log.d("BTN", "Alarm button clicked");
        });
    }
}