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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SleepArrayAdapter extends ArrayAdapter<SleepModel> {

    public SleepArrayAdapter(@NonNull Context context, @NonNull List<SleepModel> models) {
        super(context, 0, models);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.sleep_list_item_layout, parent, false);
        }
        SleepModel currentModel = getItem(position);
        TextView text = currentItemView.findViewById(R.id.sleep_list_item_clocktime_textview);
        TextView dateText = currentItemView.findViewById(R.id.sleep_list_item_date_textview);

        Random rnd = new Random();

        Date startTime = new Date((long)currentModel.getStartTimestamp() * 1000 + rnd.nextInt());
        Date endTime = new Date((long)currentModel.getEndTimestamp() * 1000 + rnd.nextInt());

        DateFormat asd = new SimpleDateFormat("EEE, dd.MM.yy");
        dateText.setText(asd.format(startTime));

        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        text.setText(String.format("%s - %s", dateFormat.format(startTime), dateFormat.format(endTime)));

        ImageView imageView = currentItemView.findViewById(R.id.sleep_list_item_success_icon);
        if (rnd.nextBoolean()) {
            imageView.setImageResource(R.drawable.ic_fail_48);
        } else {
            imageView.setImageResource(R.drawable.ic_check_48);
        }
        return currentItemView;
    }
}
