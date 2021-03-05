package com.example.sleepdiary;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class Quetionnaire extends AppCompatActivity {

    Date startTime = new Date();
    Date endTime = new Date();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quetionnaire);

        getDifference();
    }

    public void getDifference(){
        SimpleDateFormat format = new SimpleDateFormat("dd hh:mm a");
        Calendar calendar = Calendar.getInstance();

        try {
            startTime = format.parse("08:00 PM");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endTime = format.parse("04:00 AM");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long difference = startTime.getTime() - endTime.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

        Log.i("log_tag","Hours: "+hours+", Mins: "+min);

        String setDurationTv = "Hours: "+hours+", Mins: "+min;
        TextView duration_tv = (TextView)findViewById(R.id.duration_tv);
        duration_tv.setText(setDurationTv);
    }


    public void onEmoSelected(View view){
        RadioGroup radioGroup = findViewById(R.id.radioGroupQ1);
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.emoSatisfied_q1:
                break;
            case R.id.emoSmile_q1:
                break;
            case R.id.emoNull_q1:
                break;
            case R.id.emoGrimacing_q1:
                break;
            case R.id.emoDead_q1:
                break;
        }
    }

    public void onEmoSelected2(View view){
        RadioGroup radioGroup = findViewById(R.id.radioGroupQ2);
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.emoSatisfied_q2:
                break;
            case R.id.emoSmile_q2:
                break;
            case R.id.emoNull_q2:
                break;
            case R.id.emoGrimacing_q2:
                break;
            case R.id.emoDead_q2:
                break;
        }
    }

    public void q3(View view) {

        EditText editText1 = (EditText) findViewById(R.id.editNumber);
        int awakenings = Integer.parseInt(editText1.getText().toString());
    }
}