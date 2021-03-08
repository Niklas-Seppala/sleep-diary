package com.example.sleepdiary;


import android.os.Build;
import android.os.Bundle;

import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.Rating;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.GlobalData;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Question form view that opens when user starts the app
 * after starting sleep session.
 *
 * @author Niklas Seppälä
 */
public class QuetionnaireActivity extends AppCompatActivity {
    int startTimeStamp;
    int endTimeStamp;
    SleepEntry partialEntry;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_fix);

        partialEntry = GlobalData.getInstance().getPartialSleepEntries().get(0);
        endTimeStamp = DateTime.Unix.getTimestamp();
        startTimeStamp = partialEntry.getStartTimestamp();
        setDuration();
        findViewById(R.id.questionnaire_submit_btn).setOnClickListener(v -> submitClicked());
    }

    /**
     * Take the data from the form and update database.
     * Close the form
     */
    private void submitClicked() {
        String caffeineIntakeStr = ((EditText)findViewById(R.id.questionnaire_caffeine_input))
                .getText().toString();

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(caffeineIntakeStr);

        int caffeineIntake;
        if (matcher.matches()) {
            caffeineIntake = Integer.parseInt(caffeineIntakeStr);
        } else {
            displayMessage("Caffeine invalid!");
            return;
        }

        Rating quality = getRating();
        saveEntry(caffeineIntake, quality);
        displayMessage("New entry saved!");
        finish();
    }

    private void displayMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                0, 190);
        toast.show();
    }

    /**
     * Get the rating from the form
     * @return Sleep rating
     */
    private Rating getRating() {
        RadioGroup radioGroup = findViewById(R.id.quality_rb_grp);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        int checkedIndex = radioGroup.indexOfChild(findViewById(checkedId));
        return Rating.fromInt(checkedIndex+1);
    }

    /**
     * Sets duration as a string to the view.
     */
    public void setDuration() {
        int duration = endTimeStamp - startTimeStamp;
        int hours = DateTime.getHoursFromSeconds(duration);
        int minutes = DateTime.getMinutesFromSeconds(duration);
        ((TextView)findViewById(R.id.questionnaire_sleep_duration_tv))
                .setText(getString(R.string.time_h_min_dur_long, hours, minutes));
    }

    /**
     * Closes open sleep entry and updates database
     * @param caffeinIntake Caffeine intake in coffee cups
     * @param rating User sleep quality rating
     */
    public void saveEntry(int caffeinIntake, Rating rating) {
        SleepEntry completeEntry = new SleepEntry(partialEntry, endTimeStamp, rating, caffeinIntake);
        DbConnection db = new DbConnection(this);
        String[] SQL_Parameters = new String[] {Integer.toString(completeEntry.getId())};
        db.update(DbTables.sleep.TABLE_NAME, completeEntry, "id=?", SQL_Parameters);
        db.close();
    }
 }