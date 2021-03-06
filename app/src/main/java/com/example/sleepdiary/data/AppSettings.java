package com.example.sleepdiary.data;

import android.content.SharedPreferences;

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
    private boolean chartTrackOverallFeeling;
    private String username;

    private AppSettings() {
        this.use24HourClockFormat = true;
        this.openNativeAlarmClockUI = false;
        this.chartTrackCaffeine = true;
        this.chartTrackOverallFeeling = true;
    }

    public static AppSettings modify() {
        if (stagedInstance == null) {
            stagedInstance = new AppSettings();
            stagedInstance.openNativeAlarmClockUI = instance.openNativeAlarmClockUI;
            stagedInstance.use24HourClockFormat = instance.use24HourClockFormat;
            stagedInstance.chartTrackCaffeine = instance.chartTrackCaffeine;
            stagedInstance.chartTrackOverallFeeling = instance.chartTrackOverallFeeling;
        }
        return stagedInstance;
    }

    public AppSettings setValue(String flag, String val) {
        switch (flag) {
            case USERNAME:
                username = val;
                break;
            default:
                throw new IllegalArgumentException("Unknown flag");
        }
        return this;
    }

    public AppSettings setValue(String flag, boolean val) {
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
                chartTrackOverallFeeling = val;
                break;
            default:
                throw new IllegalArgumentException("Unknown flag");
        }
        return this;
    }

    public static void deserialize(SharedPreferences prefs) {
        instance.use24HourClockFormat = prefs.getBoolean(CLOCK_FORMAT, true);
        instance.openNativeAlarmClockUI = prefs.getBoolean(OPEN_NATIVE_CLOCK, false);
        instance.chartTrackCaffeine = prefs.getBoolean(CHART_TRACK_CAFFEINE, true);
        instance.chartTrackOverallFeeling = prefs.getBoolean(CHART_TRACK_OVEREALL_FEELING, true);
        instance.username = prefs.getString(USERNAME, "");
    }

    public SharedPreferences.Editor serialize(SharedPreferences.Editor editor) {
        editor.putBoolean(CLOCK_FORMAT, use24HourClockFormat);
        editor.putBoolean(OPEN_NATIVE_CLOCK, openNativeAlarmClockUI);
        editor.putBoolean(CHART_TRACK_CAFFEINE, chartTrackCaffeine);
        editor.putBoolean(CHART_TRACK_OVEREALL_FEELING, chartTrackOverallFeeling);
        editor.putString(USERNAME, username);
        return editor;
    }

    public void revert() {
        stagedInstance = null;
    }

    public AppSettings commit() {
        instance = this;
        stagedInstance = null;
        return this;
    }

    public boolean getOpenNativeClock() {
        return openNativeAlarmClockUI;
    }

    public boolean getChartTrackCaffeine() {
        return chartTrackCaffeine;
    }

    public boolean getUse24HourClockFormat() {
        return use24HourClockFormat;
    }

    public boolean getChartTrackOverallFeeling() {
        return chartTrackOverallFeeling;
    }

    public String getUsername() {
        return username;
    }
}
