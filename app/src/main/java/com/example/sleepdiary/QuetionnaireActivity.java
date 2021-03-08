package com.example.sleepdiary;

import android.os.Bundle;

import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.Rating;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.GlobalData;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.time.DateTime;

public class QuetionnaireActivity extends AppCompatActivity {
    int startTimeStamp;
    int endTimeStamp;
    SleepEntry partialEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_fix);

        partialEntry = GlobalData.getInstance().getSleepEntries().get(0); // TODO fix
        endTimeStamp = DateTime.Unix.getTimestamp();
        startTimeStamp = partialEntry.getStartTimestamp();
        setDuration();

        findViewById(R.id.questionnaire_submit_btn).setOnClickListener(v -> {

            String caffeineIntakeStr = ((EditText)findViewById(R.id.questionnaire_caffeine_input))
                    .getText().toString();

            int caffeineIntake;
            if (caffeineIntakeStr.isEmpty()) {
                caffeineIntake = 0;
            } else  {
                caffeineIntake = Integer.parseInt(caffeineIntakeStr);
            }

            Rating quality = getRating();
            saveEntry(caffeineIntake, quality);
            finish();

            // Display success message
            Toast toast = Toast.makeText(this, "New entry saved!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, 190);
            toast.show();
        });
    }

    private Rating getRating() {
        RadioGroup radioGroup = findViewById(R.id.quality_rb_grp);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        int checkedIndex = radioGroup.indexOfChild(findViewById(checkedId));
        return Rating.fromInt(checkedIndex+1);
    }

    /*Get the duration of sleep by timestamps*/
    public void setDuration() {
        int duration = endTimeStamp - startTimeStamp;
        int hours = DateTime.getHoursFromSeconds(duration);
        int minutes = DateTime.getMinutesFromSeconds(duration);
        ((TextView)findViewById(R.id.questionnaire_sleep_duration_tv))
                .setText(getString(R.string.time_h_min_dur_long, hours, minutes));
    }

    /*Save entry to app's DB*/
    public void saveEntry(int caffeinIntake, Rating rating) {

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