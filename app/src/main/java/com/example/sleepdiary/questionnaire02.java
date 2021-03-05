package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class questionnaire02 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire02);
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