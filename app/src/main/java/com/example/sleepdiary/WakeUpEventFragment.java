package com.example.sleepdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class WakeUpEventFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.popup_fragment_wake_up_event, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setButtonClicks(view);
    }

    private void setButtonClicks(View view) {
        view.findViewById(R.id.wake_up_event_ok_btn).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuetionnaireActivity.class);
            startActivity(intent);
            dismiss();
        });

        view.findViewById(R.id.wake_up_event_cancel_btn).setOnClickListener(v -> {
            dismiss();
        });
    }
}
