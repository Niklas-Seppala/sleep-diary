package com.example.sleepdiary.time;

/**
 * Class that wraps up week number, month and year.
 */
public class SimpleDate {
    private final int week;
    private final int month;
    private final int year;

    /**
     * Create an instance of SimpleDate-class.
     * @param week Week number of the year
     * @param month Month
     * @param year Year
     */
    public SimpleDate(int week, int month, int year) {
        this.week = week;
        this.month = month;
        this.year = year;
    }

    /**
     * @return Week number of the SimpleDate instance.
     */
    public int getWeek() {
        return week;
    }

    /**
     * @return Month of the SimpleDate instance.
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return Year of the SimpleDate instance
     */
    public int getYear() {
        return year;
    }
}
