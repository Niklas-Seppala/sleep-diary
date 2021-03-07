package com.example.sleepdiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * Settings fragment, that controls the input views on fragment_settings.xml
 * layout. User can interact with inputs, and only when apply-button is pressed,
 * changes are saved to disc and runtime memory.
 */
@SuppressLint("UseSwitchCompatOrMaterialCode")
@RequiresApi(api = Build.VERSION_CODES.N)
public class SettingsFragment extends Fragment {
    private EditText usernameEditText;
    private Switch clockFormatSwitch;
    private Switch nativeAlarmSwitch;
    private Switch trackCaffeineSwitch;
    private Switch trackOverallFealingSwitch;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setButtonClickHandlers(view);
        setCurrentValues();
        setSwitchHandlers();
    }

    /**
     * Find the user input views from Settings fragment.
     * @param view Settings fragmen
     */
    private void findViews(View view) {
        usernameEditText = view.findViewById(R.id.settings_edit_text_username);
        clockFormatSwitch = view.findViewById(R.id.settings_24h_format_switch);
        nativeAlarmSwitch = view.findViewById(R.id.settings_native_alarm_switch);
        trackCaffeineSwitch = view.findViewById(R.id.settings_track_caffeine_switch);
        trackOverallFealingSwitch = view.findViewById(R.id.settings_track_overall_fealing_switch);
    }

    /**
     * Set previous settings values to user inputs.
     */
    private void setCurrentValues() {
        AppSettings settings = AppSettings.getInstance();
        String username = settings.getUsername();
        if (!username.isEmpty()) {
            usernameEditText.setText(settings.getUsername());
        }
        clockFormatSwitch.setChecked(settings.getUse24HourClockFormat());
        nativeAlarmSwitch.setChecked(settings.getOpenNativeClock());
        trackCaffeineSwitch.setChecked(settings.getChartTrackCaffeine());
        trackOverallFealingSwitch.setChecked(settings.getChartTrackQualityRating());
    }

    /**
     * Set click event handlers to apply and reset buttons.
     * @param view Settings fragment
     */

    private void setButtonClickHandlers(View view) {
        view.findViewById(R.id.settings_apply_button).setOnClickListener(v -> applyChanges());
        view.findViewById(R.id.settings_reset_button).setOnClickListener(v -> revertChanges());
    }


    /**
     * Abort editing AppSettings. Reset to user input views.
     * to original values.
     */
    private void revertChanges() {
        AppSettings.modify().revert();
        setCurrentValues();
    }

    /**
     * Apply user's changes to AppSettings. Show success message.
     * Redirect user to home fragment.
     */
    private void applyChanges() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(AppSettings.NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        AppSettings.modify()
                .setValue(AppSettings.USERNAME, usernameEditText.getText().toString())
                .commit()
                .write(editor)
                .apply();

        // Get the main activity
        MainActivity main = (MainActivity)getActivity();

        // Display success message
        Toast toast = Toast.makeText(main, "Settings saved!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                0, 190);
        toast.show();

        // Redirect user to home fragment
        Objects.requireNonNull(main).setFragment(main.homeFrag);
    }

    /**
     * Sets value change eventhandlers to switch views.
     */
    private void setSwitchHandlers() {
        clockFormatSwitch.setOnCheckedChangeListener((v, checked) ->
                AppSettings.modify().setValue(AppSettings.CLOCK_FORMAT, checked));

        nativeAlarmSwitch.setOnCheckedChangeListener((v, checked) ->
                AppSettings.modify().setValue(AppSettings.OPEN_NATIVE_CLOCK, checked));

        trackCaffeineSwitch.setOnCheckedChangeListener((v, checked) ->
                AppSettings.modify().setValue(AppSettings.CHART_TRACK_CAFFEINE, checked));

        trackOverallFealingSwitch.setOnCheckedChangeListener((v, checked) ->
                AppSettings.modify().setValue(AppSettings.CHART_TRACK_OVEREALL_FEELING, checked));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}