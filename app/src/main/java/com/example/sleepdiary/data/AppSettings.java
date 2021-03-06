package com.example.sleepdiary.data;

import android.content.SharedPreferences;

public class AppSettings {
    public static final String NAME = "SETTINGS";
    public static final String CLOCK_FORMAT = "CLOCK_FORMAT";
    public static final String OPEN_NATIVE_CLOCK = "OPEN_NATIVE_CLOCK";
    public static final String CHART_TRACK_CAFFEINE = "CHART_TRACK_CAFFEINE";
    public static final String USERNAME = "USERNAME";

    private static AppSettings stagedInstance = null;
    private static AppSettings instance = new AppSettings();
    public static AppSettings getInstance() {
        return instance;
    }

    private boolean use24HourClockFormat;
    private boolean openNativeAlarmClockUI;
    private boolean chartTrackCaffeine;
    private String username;

    private AppSettings() {
        this.use24HourClockFormat = true;
        this.openNativeAlarmClockUI = false;
        this.chartTrackCaffeine = true;
    }

    public static AppSettings modify() {
        if (stagedInstance == null) {
            stagedInstance = new AppSettings();
            stagedInstance.openNativeAlarmClockUI = instance.openNativeAlarmClockUI;
            stagedInstance.use24HourClockFormat = instance.use24HourClockFormat;
            stagedInstance.chartTrackCaffeine = instance.chartTrackCaffeine;
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
            default:
                throw new IllegalArgumentException("Unknown flag");
        }
        return this;
    }

    public static void deserialize(SharedPreferences prefs) {
        instance.use24HourClockFormat = prefs.getBoolean(CLOCK_FORMAT, true);
        instance.openNativeAlarmClockUI = prefs.getBoolean(OPEN_NATIVE_CLOCK, false);
        instance.chartTrackCaffeine = prefs.getBoolean(CHART_TRACK_CAFFEINE, true);
        instance.username = prefs.getString(USERNAME, "");
    }

    public SharedPreferences.Editor serialize(SharedPreferences.Editor editor) {
        editor.putBoolean(CLOCK_FORMAT, use24HourClockFormat);
        editor.putBoolean(OPEN_NATIVE_CLOCK, openNativeAlarmClockUI);
        editor.putBoolean(CHART_TRACK_CAFFEINE, chartTrackCaffeine);
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

    public String getUsername() {
        return username;
    }
}
