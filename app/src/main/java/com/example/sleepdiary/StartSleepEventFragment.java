package com.example.sleepdiary;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.models.User;

import java.time.Instant;

public class StartSleepEventFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.popup_fragment_start_sleep_event, container,
                false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setButtonClicks(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setButtonClicks(View view) {
        view.findViewById(R.id.start_sleep_event_ok_btn).setOnClickListener(v -> {
            savePartialEntryToDB();
            dismiss();
            Toast toast = Toast.makeText(getContext(), "Good night", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, 190);
            toast.show();
        });

        view.findViewById(R.id.start_sleep_event_cancel_btn).setOnClickListener(v -> dismiss());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void savePartialEntryToDB() {
        User user = GlobalData.getInstance().getCurrentUser();
        SleepEntry entry = new SleepEntry(user.getId(),
                (int)Instant.now().getEpochSecond());
        DbConnection db = new DbConnection(getContext());
        db.insert(entry);
        db.close();
    }
}
