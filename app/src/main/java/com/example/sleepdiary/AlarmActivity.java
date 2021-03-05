package com.example.sleepdiary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextView newTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        newTextView = findViewById(R.id.textView);

        // New alarm variable.
        Button newAlarm = findViewById(R.id.button_new_alarm);
        newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment settingAlarm = new SettingAlarm();
                settingAlarm.show(getSupportFragmentManager(), "Setting alarm");
            }
        });

        // Cancel button variable, with cancel the alarm method
        Button cancelAlarm = findViewById(R.id.button_cancel);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    // TimePicker widget is opened.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        // ^ Creating c variable
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        // ^ Referring to the hourOfDay variable
        c.set(Calendar.MINUTE, minute);
        // ^ Referring to the minute variable
        c.set(Calendar.SECOND, 0);
        // ^ When the alarm goes on the seconds will be 0.
        updateTimeText(c);
        startAlarm(c);
    }

    //  timeText string. When new alarm is set the time will appear in the main view.
    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        // Adding to the timeText string the time that user picks for the alarm time.
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        newTextView.setText(timeText);
    }

    // The new alarm setting. AlarmManager is activated and the alarm will goes off at the time and date when it is set by the user.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alert.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    // Method for cancelling the alarm.
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alert.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        newTextView.setText("Set new alarm");
    }
}