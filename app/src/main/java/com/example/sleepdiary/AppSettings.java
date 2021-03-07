package com.example.sleepdiary;

import android.content.SharedPreferences;

/**
 * Singleton class for Application settings.
 * Instance fields are read/write using SharedPreferences.
 *
 * When modifying AppSettings, staged instance is returned and
 * changes made are only applied to that instance.
 *
 * When all modification are made, commit() changes staged instance
 * to "main" instance.
 */
public class AppSettings {
    public static final String NAME = "SETTINGS";
    public static final String CLOCK_FORMAT = "clockTimeFormat";
    public static final String OPEN_NATIVE_CLOCK = "openNativeClock";
    public static final String CHART_TRACK_CAFFEINE = "trackCaffeine";
    public static final String CHART_TRACK_OVEREALL_FEELING = "trackFeeleing";
    public static final String USERNAME = "username";

    private static AppSettings stagedInstance = null;
    private static AppSettings instance = new AppSettings();
    public static AppSettings getInstance() {
        return instance;
    }

    private boolean use24HourClockFormat;
    private boolean openNativeAlarmClockUI;
    private boolean chartTrackCaffeine;
    private boolean chartTrackQualityRating;
    private String username;

    /**
     * Start modifying AppSettings.
     * @return staged instance
     */
    public static AppSettings modify() {
        if (stagedInstance == null) {
            stagedInstance = new AppSettings();
            stagedInstance.openNativeAlarmClockUI = instance.openNativeAlarmClockUI;
            stagedInstance.use24HourClockFormat = instance.use24HourClockFormat;
            stagedInstance.chartTrackCaffeine = instance.chartTrackCaffeine;
            stagedInstance.chartTrackQualityRating = instance.chartTrackQualityRating;
        }
        return stagedInstance;
    }

    /**
     * Set a String value with corresponding flag.
     * @param flag Flag tied to edited value.
     * @param val String value.
     * @throws IllegalArgumentException when flag is unknown
     * @return this instance.
     *
     */
    public AppSettings setValue(String flag, String val) throws IllegalArgumentException {
        switch (flag) {
            case USERNAME:
                username = val;
                break;
            default:
                throw new IllegalArgumentException("Unknown flag");
        }
        return this;
    }

    /**
     * Set a boolean valie with corresponding flag.
     * @param flag Flag tied to edited value.
     * @param val Boolean value.
     * @throws IllegalArgumentException when flag is unknown
     * @return this instance.
     */
    public AppSettings setValue(String flag, boolean val) throws IllegalArgumentException {
        switch (flag) {
            case CLOCK_FORMAT:
                use24HourClockFormat = val;
                break;
            case OPEN_NATIVE_CLOCK:
                openNativeAlarmClockUI = val;
                break;
            case CHART_TRACK_CAFFEINE:
                chartTrackCaffeine = val;
                break;
            case CHART_TRACK_OVEREALL_FEELING:
                chartTrackQualityRating = val;
                break;
            default:
                throw new IllegalArgumentException("Unknown flag");
        }
        return this;
    }

    /**
     * Read settings from disc (settings SharedPreferences).
     * @param prefs Settings SharedPreferences instance.
     */
    public static void read(SharedPreferences prefs) {
        instance.use24HourClockFormat = prefs.getBoolean(CLOCK_FORMAT, true);
        instance.openNativeAlarmClockUI = prefs.getBoolean(OPEN_NATIVE_CLOCK, false);
        instance.chartTrackCaffeine = prefs.getBoolean(CHART_TRACK_CAFFEINE, true);
        instance.chartTrackQualityRating = prefs.getBoolean(CHART_TRACK_OVEREALL_FEELING, true);
        instance.username = prefs.getString(USERNAME, "");
    }

    /**
     * Write settings to disc (settings SharedPreferences).
     * @param editor used SharedPreferences Editor object
     * @return used SharedPreferences Editor object
     */
    public SharedPreferences.Editor write(SharedPreferences.Editor editor) {
        editor.putBoolean(CLOCK_FORMAT, use24HourClockFormat);
        editor.putBoolean(OPEN_NATIVE_CLOCK, openNativeAlarmClockUI);
        editor.putBoolean(CHART_TRACK_CAFFEINE, chartTrackCaffeine);
        editor.putBoolean(CHART_TRACK_OVEREALL_FEELING, chartTrackQualityRating);
        editor.putString(USERNAME, username);
        return editor;
    }

    /**
     * Unstage previously made changes.
     */
    public void revert() {
        stagedInstance = null;
    }

    /**
     * Commit previously made changes
     * @return this instance.
     */
    public AppSettings commit() {
        instance = this;
        stagedInstance = null;
        return this;
    }

    /**
     * @return setting for opening native clock application when setting
     *         alarm.
     */
    public boolean getOpenNativeClock() {
        return openNativeAlarmClockUI;
    }

    /**
     * @return Setting for tracking caffeine usage in chart view.
     */
    public boolean getChartTrackCaffeine() {
        return chartTrackCaffeine;
    }

    /**
     * @return Setting for using 24-hour clock format when setting alarm clock.
     */
    public boolean getUse24HourClockFormat() {
        return use24HourClockFormat;
    }

    /**
     * @return Setting for tracking sleep quality rating usage in chart view.
     */
    public boolean getChartTrackQualityRating() {
        return chartTrackQualityRating;
    }

    /**
     * @return Username.
     */
    public String getUsername() {
        return username;
    }
}
