package com.example.sleepdiary;

import android.os.Bundle;

import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.Rating;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.GlobalData;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;


public class QuetionnaireActivity extends AppCompatActivity {

    // change all to private
    TextView heading;
    TextView sleepDuration_tv;
    TextView duration_tv;
    TextView q1;
    RadioGroup radioGroupQ1;
    TextView q3;
    EditText editNumber;
    TextView q3_2;
    Button submitBtn;
    Rating rating;
    int startTimeStamp;
    int endTimeStamp;
    int caffeinIntake;

    SleepEntry partialEntry;

/*Initial activity view*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quetionnaire);

        // We need to get the latest entry that is partial (endTimestamp is set to -1)
        // this is handled by MainActivity, that gets us here.
        partialEntry = GlobalData.getInstance().getSleepEntries().get(0); // list is in reverse order, so latest is at 0

        heading = (TextView) findViewById(R.id.heading02_tv);
        sleepDuration_tv = (TextView) findViewById(R.id.sleepDuration_tv);
        duration_tv = (TextView) findViewById(R.id.duration_tv);
        q1 = (TextView) findViewById(R.id.q1);
        radioGroupQ1 = (RadioGroup) findViewById(R.id.radioGroupQ1);
        q3 = (TextView) findViewById(R.id.q3);
        q3_2 = (TextView) findViewById(R.id.q3_2);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(submit);
        radioGroupQ1 = findViewById(R.id.radioGroupQ1);
        editNumber = (EditText) findViewById(R.id.editNumber);

        getDuration();
    }

    /*Get the duration of sleep by timestamps*/
    public void getDuration() {
        startTimeStamp = partialEntry.getStartTimestamp();

        // Sleep entry is partial, and has no valid endTimestamp (-1)
        // get current time using DateTime.Unix
        endTimeStamp = DateTime.Unix.getTimestamp();

        int duration = endTimeStamp - startTimeStamp;
        int hours = DateTime.getHoursFromSeconds(duration);
        int minutes = DateTime.getMinutesFromSeconds(duration);
        duration_tv.setText(getString(R.string.time_h_min_dur_long, hours, minutes));

    }

    /* submitting the questionnaire*/
    private View.OnClickListener submit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            q3();
            onEmoSelected(radioGroupQ1,0);
            saveEntry();
            finish();
        }
    };

/* Radiogroup, return sleep quality to int 0-5*/
        public void onEmoSelected (RadioGroup group,int checkedId){
        RadioGroup radioGroup = findViewById(R.id.radioGroupQ1);

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.emoVerySatisfied_q1:
                rating = Rating.fromInt(5);
                break;
            case R.id.emoSatisfied_q1:
                rating = Rating.fromInt(4);
                break;
            case R.id.emoNeutral_q1:
                rating = Rating.fromInt(3);
                break;
            case R.id.emoDissatisfied_q1:
                rating = Rating.fromInt(2);
                break;
            case R.id.emoVeryDissatisfied_q1:
                rating = Rating.fromInt(1);
                break;
            default:
                rating = Rating.fromInt(0);
                break;
        }

    }

/* Read user input*/
    public void q3() {
        caffeinIntake = Integer.parseInt(editNumber.getText().toString());
    }

    /*Save entry to app's DB*/
    public void saveEntry() {

        // Create new Entry based on partial entry (the one we are finishing)
        // and fill the missing data
        SleepEntry completeEntry = new SleepEntry(partialEntry, endTimeStamp, rating, caffeinIntake);

        DbConnection db = new DbConnection(this);

        // Parameters for UPDATE query, in this case the sleepEntry id, as a String
        String[] SQL_Parameters = new String[] {Integer.toString(completeEntry.getId())};

        // pass the completed entry to update function, with the WHERE clause and parameters for that clause
        db.update(DbTables.sleep.TABLE_NAME, completeEntry, "id=?", SQL_Parameters);

        db.close();
    }

 }