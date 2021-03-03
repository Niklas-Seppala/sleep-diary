package com.example.sleepdiary.time;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Static utility class for dealing with time and dates.
 */
public abstract class DateTime {
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat timeFormat = new SimpleDateFormat("kk:mm");
    private static final Calendar calendar = Calendar.getInstance(Locale.GERMANY);

    public static final int SECONDS_IN_HOUR = 3600;

    /**
     * This enum represents a weekday. Week starts on Monday and ends
     * on Sunday. Indexes start at 0.
     */
    public enum IndexedWeekDay {
        MON(0), TUE(1), WED(2), THU(3),
        FRI(4), SAT(5), SUN(6);

        private final int value;
        IndexedWeekDay(int val){
            this.value = val;
        }

        /**
         * @return Index of the weekday.
         */
        public int getIndex() {
            return value;
        }
    }

    /**
     * Creates a clock time range string from two dates.
     *      example: "16:42 - 17:25"
     *
     * @param start start Date object
     * @param end end Date object
     * @return Time of day range as a string.
     */
    public static String timeOfDayRangeToString(Date start, Date end) {
        return String.format("%s - %s", timeFormat.format(start), timeFormat.format(end));
    }

    /**
     * Creates a time string from seconds. Only hours and minutes
     * can be used.
     *      Example: "13h, 32min"
     * @param format Optional format
     * @param seconds seconds
     * @return Time string containing hours and minutes
     */
    public static String secondsToTimeString(@Nullable String format, int seconds) {
        int h = getHoursFromSeconds(seconds);
        int min = getMinutesFromSeconds(seconds);

        format = format == null ? "%dh, %dmin" : format;
        return String.format(format, h, min);
    }

    public static int getMinutesFromSeconds(int seconds) {
        return (seconds % SECONDS_IN_HOUR) / 60;
    }

    public static int getHoursFromSeconds(int seconds) {
        return seconds / SECONDS_IN_HOUR;
    }

    /**
     * Utility functions related to Unix time
     */
    public static class Unix {

        /**
         * Converts Unix time timestamp to indexed weekday.
         * @param unix unix epoch timestamp
         * @return Indexed Weekday
         */
        public static IndexedWeekDay getWeekDay(int unix) {
            calendar.setTimeInMillis(Unix.getMillis(unix));
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:    return IndexedWeekDay.MON;
                case Calendar.TUESDAY:   return IndexedWeekDay.TUE;
                case Calendar.WEDNESDAY: return IndexedWeekDay.WED;
                case Calendar.THURSDAY:  return IndexedWeekDay.THU;
                case Calendar.FRIDAY:    return IndexedWeekDay.FRI;
                case Calendar.SATURDAY:  return IndexedWeekDay.SAT;
                default:                 return IndexedWeekDay.SUN;
            }
        }

        /**
         * Convert unix timestamp using seconds to one using milliseconds.
         * @param unixTimestamp timestamp in seconds
         * @return Unix timestamp in milliseconds
         */
        public static long getMillis(int unixTimestamp) {
            return (long)unixTimestamp * 1000;
        }

        /**
         * Creates a date object from Unix timestamp
         * @param unixTimestamp unix timestamp in seconds
         * @return Date instance
         */
        public static Date createDate(int unixTimestamp) {
            return new Date((long)unixTimestamp * 1000);
        }
    }
}
