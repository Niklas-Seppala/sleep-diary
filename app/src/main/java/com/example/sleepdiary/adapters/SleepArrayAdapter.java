package com.example.sleepdiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepdiary.R;
import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Custom ArrayAdapter for SleepModels.
 */
public class SleepArrayAdapter extends ArrayAdapter<SleepEntry> {
    public SleepArrayAdapter(@NonNull Context context, @NonNull List<SleepEntry> models) {
        super(context, 0, models);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                .inflate(R.layout.sleep_list_item_layout, parent, false);
        }

        SleepEntry entry = getItem(position);
        Date startTime = DateTime.Unix.createDate(entry.getStartTimestamp());
        Date endTime = DateTime.Unix.createDate(entry.getEndTimestamp());

        // Views
        ImageView successIcon = itemView.findViewById(R.id.sleep_list_item_success_icon);
        TextView dateText = itemView.findViewById(R.id.sleep_list_item_date_textview);
        TextView timeText = itemView.findViewById(R.id.sleep_list_item_clocktime_textview);
        TextView durText = itemView.findViewById(R.id.sleep_list_item_duration_textview);

        int duration = entry.getEndTimestamp() - entry.getStartTimestamp();

        // Set data to views
        dateText.setText(getContext().getString(R.string.time_date_time_cal, startTime));
        durText.setText(getContext().getString(R.string.time_h_min_dur_short,
                DateTime.getHoursFromSeconds(duration),
                DateTime.getMinutesFromSeconds(duration)));
        timeText.setText(getContext().getString(R.string.time_timerange, startTime, endTime));
        setSuccessIcon(successIcon, entry);

        return itemView;
    }

    /**
     *
     * @param imageView
     * @param entry
     */
    private void setSuccessIcon(ImageView imageView, SleepEntry entry) {
        int duration = entry.getEndTimestamp() - entry.getStartTimestamp();
        User user = GlobalData.getInstance().getCurrentUser();
        if (user != null) {
            int iconId;
            if (duration >= user.getGoal()) {
                iconId = R.drawable.ic_check_48;
            } else {
                iconId = R.drawable.ic_fail_48;
            }
            imageView.setImageResource(iconId);
        }
    }
}
