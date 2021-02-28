package com.example.sleepdiary.data;

public class WeeklySleepHabit {
    private final int weekNum;
    private final int year;
    private final SleepModel[] days;

    public WeeklySleepHabit(int weekNum, int year, SleepModel[] days) {
        this.weekNum = weekNum;
        this.year = year;
        this.days = days;
    }

    public SleepModel[] getDays() {
        return days;
    }

    public int getYear() {
        return year;
    }

    public int getWeekNum() {
        return weekNum;
    }
}
