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
        TextView text = currentItemView.findViewById(R.id.sleep_list_item_text);

        Random rnd = new Random();

        Date startTime = new Date((long)currentModel.getStartTimestamp() * 1000 + rnd.nextInt());
        Date endTime = new Date((long)currentModel.getEndTimestamp() * 1000 + rnd.nextInt());

        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        text.setText(String.format("%s - %s", dateFormat.format(startTime), dateFormat.format(endTime)));

        ImageView imageView = currentItemView.findViewById(R.id.successIcon);
        if (rnd.nextBoolean()) {
            imageView.setImageResource(R.drawable.ic_fail_48);
        } else {
            imageView.setImageResource(R.drawable.ic_check_48);
        }

        ImageView trendingIcon = currentItemView.findViewById(R.id.trendingIcon);
        if (rnd.nextBoolean()) {
//            trendingIcon.setImageResource(R.drawable.ic_baseline_trending_down_24);
            trendingIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        } else {
//            trendingIcon.setImageResource(R.drawable.ic_baseline_trending_up_24);
            trendingIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
        }
        return currentItemView;
    }
}
