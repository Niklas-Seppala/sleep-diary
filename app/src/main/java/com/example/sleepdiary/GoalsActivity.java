package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * In this view, users can modify their private goals.
 */
public class GoalsActivity extends AppCompatActivity {
    private Button submitBtn;
    private TextView hoursInput;
    private TextView minsInput;
    private TextView caffeineInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        findViews();
        submitBtn.setOnClickListener(v -> trySubmitChanges());
    }

    /**
     * Find user inputs in the goals view.
     */
    private void findViews() {
        submitBtn = findViewById(R.id.goals_submit_btn);
        hoursInput = findViewById(R.id.goals_hours_input);
        minsInput = findViewById(R.id.goals_mins_input);
        caffeineInput = findViewById(R.id.goals_caffeine_input);
    }

    private void trySubmitChanges() {
        if (!validate()) return;

        // Parse values and change them to correct format (time is in seconds)
        int hoursInSeconds = Integer.parseInt(hoursInput.getText().toString()) * 60 * 60;
        int minsInSeconds = Integer.parseInt(minsInput.getText().toString()) * 60;
        int totalSeconds = hoursInSeconds + minsInSeconds;
        int cup = Integer.parseInt(caffeineInput.getText().toString());

        // Create new user data model and update it based on old and new values
        User currentUser = GlobalData.getInstance().getCurrentUser();
        User newUser = new User(currentUser.getId(), currentUser.getName(), totalSeconds, cup);

        // Update database
        DbConnection db = new DbConnection(this);
        String[] params = { Integer.toString(currentUser.getId()) };
        db.update(DbTables.user.TABLE_NAME, newUser, "id=?", params);
        db.close();

        // Display success message
        Toast toast = Toast.makeText(this, "Goals updated", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                0, 190);
        toast.show();

        // Back to home fragment
        finish();
    }

    /**
     * Validate single user input result.
     * @param field field result as a String.
     * @param pattern Regex pattern.
     * @param errMsg Error message that will be displayed when field validation.
     *               fails
     * @return true if valid.
     */
    private boolean validateField(String field, Pattern pattern, String errMsg) {
        Matcher matcher = pattern.matcher(field);
        if (!matcher.matches()) {
            Toast toast = Toast.makeText(this, errMsg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0, 190);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * Validates all the user inputs goals view.
     * @return true if all fields are valid
     */
    private boolean validate() {
        Pattern pattern = Pattern.compile("[0-9]"); // only match numeric values
        String hours = hoursInput.getText().toString();
        String mins = minsInput.getText().toString();
        String cups = caffeineInput.getText().toString();

        return validateField(hours, pattern, "Hours value is invalid!") &&
                validateField(mins,  pattern, "Minutes value is invalid!")  &&
                validateField(cups,  pattern, "Caffeine value is invalid!");
    }
}