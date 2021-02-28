package com.example.sleepdiary.data;

import androidx.annotation.NonNull;

import java.util.List;

public class SleepHabits {
    private int index;
    private final int weekCount;
    private final List<WeeklySleepHabit> weeks;

    public SleepHabits(@NonNull List<WeeklySleepHabit> weeklySleepHabits) {
        weeks = weeklySleepHabits;
        index = 0;
        weekCount = weeklySleepHabits.size();
    }

    public SleepHabits(@NonNull List<WeeklySleepHabit> weeklySleepHabits, int savedIndex) {
        this(weeklySleepHabits);
        index = savedIndex;
    }

    public SleepHabits nextWeek() {
        if (index +1 < this.weekCount)
            index++;
        return this;
    }

    public SleepHabits previousWeek() {
        if (index -1 >= 0)
            index--;
        return this;
    }

    public boolean hasNextWeek() {
        return index < weekCount -1;
    }

    public boolean hasPrevWeek() {
        return index > 0;
    }

    public WeeklySleepHabit getWeek() {
        return weeks.get(index);
    }

    public int getIndex() {
        return index;
    }
}
