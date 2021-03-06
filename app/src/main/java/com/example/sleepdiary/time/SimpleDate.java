package com.example.sleepdiary.time;

public class SimpleDate {
    private final int week;
    private final int month;
    private final int year;

    public SimpleDate(int week, int month, int year) {
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
