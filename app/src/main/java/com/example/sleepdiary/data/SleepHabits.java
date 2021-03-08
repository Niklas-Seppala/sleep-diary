package com.example.sleepdiary.data;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * This class contains user's sleep habits, that
 * can be browsed by week.
 */
public class SleepHabits {
    private final int weekCount;
    private final List<WeeklySleepHabit> weeks;
    private int index;

    /**
     * Intantiate SleepHabit object.
     *
     * @param weeklySleepHabits List of WeeklySleepHabits.
     */
    public SleepHabits(@NonNull List<WeeklySleepHabit> weeklySleepHabits) {
        weeks = weeklySleepHabits;
        index = 0;
        weekCount = weeklySleepHabits.size();
    }

    /**
     * Intantiate SleepHabit object, with saved index state.
     *
     * @param weeklySleepHabits List of WeeklySleepHabits.
     * @param savedIndex        saved week index.
     */
    public SleepHabits(@NonNull List<WeeklySleepHabit> weeklySleepHabits, int savedIndex) {
        this(weeklySleepHabits);
        index = Math.min(savedIndex, weekCount-1);
    }

    /**
     * Move to next week.
     *
     * @return this instance.
     */
    public SleepHabits nextWeek() {
        if (index + 1 < this.weekCount)
            index++;
        return this;
    }

    /**
     * Move to previous week
     *
     * @return this instance
     */
    public SleepHabits previousWeek() {
        if (index - 1 >= 0)
            index--;
        return this;
    }

    /**
     * @return Check if there is next week available.
     */
    public boolean hasNextWeek() {
        return index < weekCount - 1;
    }

    /**
     * @return Check if there is previous week available.
     */
    public boolean hasPrevWeek() {
        return index > 0;
    }

    /**
     * @return Get the current week sleep habits.
     */
    public WeeklySleepHabit getWeek() {
        return weeks.get(index);
    }

    /**
     * @return Get the current week index.
     */
    public int getIndex() {
        return index;
    }
}
