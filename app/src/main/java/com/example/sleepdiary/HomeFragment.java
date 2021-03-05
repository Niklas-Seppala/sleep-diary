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
        setHomeViewButtonEvents();
    }

    // Dont touch!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void setHomeViewButtonEvents() {

        // STATS BUTTON
        getActivity().findViewById(R.id.btnStats).setOnClickListener(view -> {
            Log.d("BTN", "Stats button clicked");
        });

        // SLEEP BUTTON
        getActivity().findViewById(R.id.btnSleep).setOnClickListener(view -> {
            Log.d("BTN", "Sleep button clicked");

        });

        // GOAL BUTTON
        getActivity().findViewById(R.id.btnGoal).setOnClickListener(view -> {
            Log.d("BTN", "Goal button clicked");
        });

        // ALARM BUTTOn
        getActivity().findViewById(R.id.btnAlarm).setOnClickListener(view -> {
            Log.d("BTN", "Alarm button clicked");
        });
    }
}