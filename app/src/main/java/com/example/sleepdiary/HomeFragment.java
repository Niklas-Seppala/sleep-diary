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
            Log.d("BTN", "Goal button clicked");
        });
        // Alarm Buttom Click
        view.findViewById(R.id.btnAlarm).setOnClickListener(v -> {
            Intent alarm = new Intent(getView().getContext(), AlarmActivity.class);
            startActivity(alarm);
        });
    }
}