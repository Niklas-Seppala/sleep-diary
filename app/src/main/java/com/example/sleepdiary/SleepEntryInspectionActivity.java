package com.example.sleepdiary;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepdiary.adapters.SleepRatingIconService;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SleepEntryInspectionActivity extends AppCompatActivity {
    public static final String EXTRA_ENTRY_INDEX = "ENTRY_INDEX";

    TextView dateTextView;
    TextView durTextView;
    TextView timeRangeTextView;
    ImageView successIconImageView;
    ImageView ratingImageView;
    ImageView ratingGrowthImageView;
    ImageView durationIncreaseImageView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry_inspection);

        int entryIndex = getIntent().getExtras().getInt(EXTRA_ENTRY_INDEX);

        initViews();
        SleepEntry entry = getEntry(entryIndex);
        setDate(entry);
        setTimeRange(entry);
        setDuration(entry, entryIndex);
        setRating(entry, entryIndex);
        setCaffeineIntake(entry, entryIndex);
        setSuccessIcon(entry);
    }

    private void setSuccessIcon(SleepEntry entry) {
        int duration = entry.getEndTimestamp() - entry.getStartTimestamp();
        User user = GlobalData.getInstance().getCurrentUser();
        if (user != null) {
            int iconId;
            if (duration >= user.getGoal()) {
                iconId = R.drawable.ic_check_48;
            } else {
                iconId = R.drawable.ic_fail_48;
            }
            successIconImageView.setImageResource(iconId);
        }
    }

    private void initViews() {
        dateTextView = findViewById(R.id.sleep_entry_inspect_date_tv);
        durTextView = findViewById(R.id.sleep_entry_inspect_dur_tv);
        timeRangeTextView = findViewById(R.id.sleep_entry_inspection_timerange_tv);
        ratingImageView = findViewById(R.id.sleep_entry_inspection_rating_iv);
        ratingGrowthImageView = findViewById(R.id.sleep_entry_inspection_rating_growth_iv);
        successIconImageView = findViewById(R.id.sleep_entry_inspection_success_icon_iv);
        durationIncreaseImageView = findViewById(R.id.sleep_etnry_inspection_duration_increase_icon_iv);
    }

    private void setRating(SleepEntry entry, int entryIndex) {
        Drawable ratingIcon = SleepRatingIconService.getIconFromRating(
                this, entry.getQuality());
        ratingImageView.setImageDrawable(ratingIcon);

        // Set growth icon based on previous item, if there is none, still growing
        if (entry.getQuality().toInt() >= getPreviousEntry(entryIndex).getQuality().toInt()) {
            ratingGrowthImageView.setImageResource(R.drawable.ic_baseline_expand_less_48);
        }
        else {
            ratingGrowthImageView.setImageResource(R.drawable.ic_baseline_expand_more_48);
        }
    }

    private SleepEntry getPreviousEntry(int index) {
        int collectionSize = GlobalData.getInstance().getSleepEntries().size();
        int prevEntryIndex = Math.min(index + 1, collectionSize - 1);
        return GlobalData.getInstance().getSleepEntries().get(prevEntryIndex);
    }

    private SleepEntry getEntry(int entryIndex) {
        return GlobalData.getInstance().getSleepEntries().get(entryIndex);
    }

    private void setCaffeineIntake(SleepEntry entry, int entryIndex) {

    }

    private void setTimeRange(SleepEntry entry) {
        Date start = new Date(entry.getStartTimestamp());
        Date end = new Date(entry.getEndTimestamp());
        timeRangeTextView.setText(getString(R.string.time_timerange, start, end));
    }

    private void setDuration(SleepEntry entry, int index) {
        int duration = entry.getEndTimestamp() - entry.getStartTimestamp();
        int hours = DateTime.getHoursFromSeconds(duration);
        int minutes = DateTime.getMinutesFromSeconds(duration);
        durTextView.setText(getString(R.string.time_h_min_dur_long, hours, minutes));

        SleepEntry prevEntry = getPreviousEntry(index);
        int prevDuration = prevEntry.getEndTimestamp() - prevEntry.getStartTimestamp();
        int iconId;
        if (duration >= prevDuration) {
            iconId = R.drawable.ic_baseline_expand_less_48;
        } else {
            iconId = R.drawable.ic_baseline_expand_more_48;
        }
        durationIncreaseImageView.setImageResource(iconId);
    }

    private void setDate(SleepEntry entry) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(DateTime.Unix.getMillis(entry.getStartTimestamp()));
        dateTextView.setText(getString(R.string.time_date_time_cal, cal));
    }
}
