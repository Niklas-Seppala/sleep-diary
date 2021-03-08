package com.example.sleepdiary;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/* This is SettingAlarm class. */
public class SettingAlarm extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),
                (TimePickerDialog.OnTimeSetListener)getActivity(), /* This picks the time what the user those to use. */
                hour, minute,
                AppSettings.getInstance().getUse24HourClockFormat()); /* Phone is set to use 24/7 clock.*/
    }
}