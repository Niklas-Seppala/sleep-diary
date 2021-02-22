package com.example.sleepdiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepdiary.data.SleepModel;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

/**
 * Custom ArrayAdapter for SleepModels.
 */
public class SleepArrayAdapter extends ArrayAdapter<SleepModel> {
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("EEE, dd.MM.yy");
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat timeFormat = new SimpleDateFormat("kk:mm");

    public SleepArrayAdapter(@NonNull Context context, @NonNull List<SleepModel> models) {
        super(context, 0, models);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext())
                .inflate(R.layout.sleep_list_item_layout, parent, false);
        }
        // Model data
        SleepModel model = getItem(position);
        Date startTime = createDateFromUnix(model.getStartTimestamp());
        Date endTime = createDateFromUnix(model.getEndTimestamp());
        Date duration = createDateFromUnix(model.getEndTimestamp() - model.getStartTimestamp());

        // Views
        ImageView successIcon = itemView.findViewById(R.id.sleep_list_item_success_icon);
        TextView dateText = itemView.findViewById(R.id.sleep_list_item_date_textview);
        TextView timeText = itemView.findViewById(R.id.sleep_list_item_clocktime_textview);
        TextView durText = itemView.findViewById(R.id.sleep_list_item_duration_textview);

        int diff = model.getEndTimestamp() - model.getStartTimestamp();
        int h = diff / 3600;
        int min = (diff % 3600) / 60;

        // Set data to views
        dateText.setText(dateFormat.format(startTime));
        durText.setText(String.format("%dh %dmin", h, min));
        timeText.setText(formatTimeRange(startTime, endTime));
        setSuccessIcon(successIcon, model);

        return itemView;
    }

    private String formatTimeRange(Date start, Date end) {
        return String.format("%s - %s",
                timeFormat.format(start),
                timeFormat.format(end));
    }

    private Date createDateFromUnix(int unix) {
        return new Date((long)unix * 1000);
    }

    private void setSuccessIcon(ImageView view, SleepModel model) {
        view.setImageResource(model.getEndTimestamp() - model.getStartTimestamp() < 28000
                ? R.drawable.ic_fail_48
                : R.drawable.ic_check_48);
    }
}
