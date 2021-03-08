package com.example.sleepdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;


public class InfoFragment extends Fragment {
    // Use this :)
    private TextView text;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text = (TextView) view.findViewById(R.id.textView3);
        String[] tips = getResources().getStringArray(R.array.tips_array);
        // Get a random item from the list
        Random random = new Random();
        text.setText(tips[random.nextInt(tips.length)]);
    }

    // Dont touch!
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
}