package com.example.sleepdiary.data;

/**
 * This class wraps week's sleep entries to
 * a week.
 */
public class WeeklySleepHabit {
    private final int weekNum;
    private final int year;
    private final SleepModel[] days;

    public WeeklySleepHabit(int weekNum, int year, SleepModel[] days) {
        this.weekNum = weekNum;
        this.year = year;
        this.days = days;
    }

    /**
     * @return Get the sleep entries of this week.
     */
    public SleepModel[] getDays() {
        return days;
    }

    /**
     * @return Get the year of the week.
     */
    public int getYear() {
        return year;
    }

    /**
     * @return Get the week number of it's year.
     */
    public int getWeek() {
        return weekNum;
    }
}
