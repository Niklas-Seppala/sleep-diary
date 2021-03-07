package com.example.sleepdiary.data;

import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.time.SimpleDate;

/**
 * This class wraps week's sleep entries to
 * a week.
 */
public class WeeklySleepHabit {
    private final SimpleDate date;
    private final SleepEntry[] days;

    /**
     * Creates an instance of WeeklySleepHabit-class.
     * @param date SimpleDate object that contains week and year
     * @param days SleepEntries array of the week
     */
    public WeeklySleepHabit(SimpleDate date, SleepEntry[] days) {
        this.date = date;
        this.days = days;
    }

    /**
     * @return Get the sleep entries of this week.
     */
    public SleepEntry[] getDays() {
        return days;
    }

    /**
     * @return Get the date of the week.
     */
    public SimpleDate getDate() {
        return date;
    }
}
