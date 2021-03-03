package com.example.sleepdiary;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.time.DateTime;

import java.util.Calendar;
import java.util.Locale;

public class SleepEntryInspectionActivity extends AppCompatActivity {
    public static final String EXTRA_ENTRY_INDEX = "ENTRY_INDEX";

    private Calendar calendar;

    TextView dateTextView;
    TextView durTextView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry_inspection);

        initViews();
        SleepEntry entry = getEntry();
        setDate(entry);
        setDuration(entry);
    }

    private void initViews() {
        dateTextView = findViewById(R.id.sleep_entry_inspect_date_tv);
        durTextView = findViewById(R.id.sleep_entry_inspect_dur_tv);
    }

    private SleepEntry getEntry() {
        return GlobalData.getInstance()
                .getSleepEntries()
                .get(getIntent().getExtras().getInt(EXTRA_ENTRY_INDEX, -1));
    }

    private void setDuration(SleepEntry entry) {
        int timeDiff = entry.getEndTimestamp() - entry.getStartTimestamp();
        int hours = DateTime.getHoursFromSeconds(timeDiff);
        int minutes = DateTime.getMinutesFromSeconds(timeDiff);
        durTextView.setText(getString(R.string.h_min_dur_long, hours, minutes));
    }

    private void setDate(SleepEntry entry) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(DateTime.Unix.getMillis(entry.getStartTimestamp()));
        dateTextView.setText(getString(R.string.date_time_cal, cal));
    }
}
