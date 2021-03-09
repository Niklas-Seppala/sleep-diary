package com.example.sleepdiary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepdiary.adapters.SleepRatingIconService;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity that is launced when user is inspecting single
 * SleepEntry.
 */
public class SleepEntryInspectionActivity extends AppCompatActivity {
    public static final String EXTRA_ENTRY_INDEX = "ENTRY_INDEX";
    private TextView dateTextView;
    private TextView durTextView;
    private TextView timeRangeTextView;
    private TextView caffeineTextView;
    private ImageView successIconImageView;
    private ImageView ratingImageView;
    private ImageView ratingGrowthImageView;
    private ImageView durationIncreaseImageView;
    private ImageView caffeineGrowthImageView;
    private Button deleteButton;

    private SleepEntry inspectedEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_entry_inspection);

        int entryIndex = getIntent().getExtras().getInt(EXTRA_ENTRY_INDEX);

        findViews();
        inspectedEntry = getEntry(entryIndex);
        setDate(inspectedEntry);
        setTimeRange(inspectedEntry);
        setDuration(inspectedEntry, entryIndex);
        setRating(inspectedEntry, entryIndex);
        setCaffeineIntake(inspectedEntry, entryIndex);
        setSuccessIcon(inspectedEntry);

        setBtnClicks();
    }

    /**
     * Set success/fail icon based on sleep duration.
     * If duration is bellow user set goal, display.
     * fail icon, else success icon.
     * @param entry Inspected SleepEntry object.
     */
    private void setSuccessIcon(SleepEntry entry) {
        int duration = entry.getEndTimestamp() - entry.getStartTimestamp();
        User user = GlobalData.getInstance().getCurrentUser();
        if (user != null) {
            int iconId;
            if (duration >= user.getSleepGoal()) {
                iconId = R.drawable.ic_check_48;
            } else {
                iconId = R.drawable.ic_fail_48;
            }
            successIconImageView.setImageResource(iconId);
        }
    }

    /**
     * Find views from layout.
     */
    private void findViews() {
        dateTextView = findViewById(R.id.sleep_entry_inspect_date_tv);
        durTextView = findViewById(R.id.sleep_entry_inspect_dur_tv);
        timeRangeTextView = findViewById(R.id.sleep_entry_inspection_timerange_tv);
        caffeineTextView = findViewById(R.id.sleep_entry_inspection_caffeine_tv);

        ratingImageView = findViewById(R.id.sleep_entry_inspection_rating_iv);
        ratingGrowthImageView = findViewById(R.id.sleep_entry_inspection_rating_growth_iv);
        successIconImageView = findViewById(R.id.sleep_entry_inspection_success_icon_iv);
        durationIncreaseImageView = findViewById(R.id.sleep_etnry_inspection_duration_increase_icon_iv);
        caffeineGrowthImageView = findViewById(R.id.sleep_entry_inspection_caffeine_growth_iv);
        deleteButton = findViewById(R.id.sleep_entry_inspection_delete_btn);
    }

    private void setBtnClicks() {
        deleteButton.setOnClickListener(view -> {
            DbConnection db = new DbConnection(this);

            String[] params = {Integer.toString(inspectedEntry.getId())};
            db.delete(DbTables.sleep.TABLE_NAME, "id=?", params);
            db.close();

            // Display success message
            Toast toast = Toast.makeText(this, "Entry deleted!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, 190);
            toast.show();

            finish();
        });
    }

    /**
     * Set the sleep quality rating icon.
     * @param entry Inspected SleepEntry object.
     * @param entryIndex Index of the entry in GlobalData.
     */
    private void setRating(SleepEntry entry, int entryIndex) {
        Drawable ratingIcon = SleepRatingIconService.getIconFromRating(this,
                entry.getQuality());
        ratingImageView.setImageDrawable(ratingIcon);

        // Set growth icon based on previous item, if there is none, still growing
        if (entry.getQuality().toInt() >= getPreviousEntry(entryIndex).getQuality().toInt()) {
            ratingGrowthImageView.setImageResource(R.drawable.ic_baseline_expand_less_48);
        }
        else {
            ratingGrowthImageView.setImageResource(R.drawable.ic_baseline_expand_more_48);
        }
    }

    /**
     * Get the previous SleepEntry object from GlobalData.
     *
     * @param index Index of the inspected SleepEntry object.
     * @return Previous SleepEntry object.
     */
    private SleepEntry getPreviousEntry(int index) {
        int collectionSize = GlobalData.getInstance().getSleepEntries().size();
        int prevEntryIndex = Math.min(index + 1, collectionSize - 1);
        return GlobalData.getInstance().getSleepEntries().get(prevEntryIndex);
    }

    /**
     * Shorthand function to get Entry from GlobalData singleton.
     * @param entryIndex Index of the entry.
     * @return SleepEntry from GlobalData singleton.
     */
    private SleepEntry getEntry(int entryIndex) {
        return GlobalData.getInstance().getSleepEntries().get(entryIndex);
    }

    /**
     * Set caffeine intake to view as a String.
     * @param entry inspected sleep entry.
     * @param entryIndex index of the inspected entry.
     */
    private void setCaffeineIntake(SleepEntry entry, int entryIndex) {
        caffeineTextView.setText(getString(R.string.sleep_inspection_caffeine_amount,
                entry.getCaffeineIntake()));
        SleepEntry prevEntry = getPreviousEntry(entryIndex);
        int iconId;
        if (entry.getCaffeineIntake() >= prevEntry.getCaffeineIntake()) {
            iconId = R.drawable.ic_baseline_expand_less_48;
        } else {
            iconId = R.drawable.ic_baseline_expand_more_48;
        }
        caffeineGrowthImageView.setImageResource(iconId);
    }

    /**
     * Set Inspected SleepEntry object's sleep duration
     * time range as a text. eg. "21:11 - 07:33".
     * @param entry Inspected SleepEntry object.
     */
    private void setTimeRange(SleepEntry entry) {
        Date start = DateTime.Unix.createDate(entry.getStartTimestamp());
        Date end = DateTime.Unix.createDate(entry.getEndTimestamp());
        timeRangeTextView.setText(getString(R.string.time_timerange, start, end));
    }

    /**
     * Set inspected SleepEntry object's sleep duration as hours and minutes.
     * @param entry Inspected SleepEntry object.
     * @param index Index of the inspected SleepEntry object.
     */
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

    /**
     * Set date of the inspected SleepEntry object as a string.
     * @param entry Inspected SleepEntry object.
     */
    private void setDate(SleepEntry entry) {
        Calendar cal = Calendar.getInstance(Locale.GERMAN); // EU
        cal.setTimeInMillis(DateTime.Unix.getMillis(entry.getStartTimestamp()));
        dateTextView.setText(getString(R.string.time_date_time_cal, cal));
    }
}
