package com.example.sleepdiary.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sleepdiary.data.db.DbTables;
import com.example.sleepdiary.data.db.DbConnection;
import com.example.sleepdiary.data.models.Rating;
import com.example.sleepdiary.data.models.SleepEntry;
import com.example.sleepdiary.data.models.User;
import com.example.sleepdiary.time.DateTime;
import com.example.sleepdiary.time.SimpleDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Singleton class for accessing data loaded from database
 * globally.
 *
 * Must be initialized at the start of the application
 * GlobalData.init(DbConnection db).
 * After that, keep in sync with database changes with
 * GlobalData.update(DbConnection db).
 */
public class GlobalData {
    private static final GlobalData instance = new GlobalData();
    private static boolean isDirty = true;
    private ArrayList<User> userModels;
    private ArrayList<SleepEntry> sleepEntries;
    private List<WeeklySleepHabit> sleepModelsByWeeks;

    /**
     * @return Get all of the sleep entries.
     */
    @NonNull
    public ArrayList<SleepEntry> getSleepEntries() {
        return sleepEntries;
    }

    /**
     * @return List of completed sleepEntries.
     */
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<SleepEntry> getCompletedSleepEntries() {
        return sleepEntries.stream()
                .filter(entry -> !entry.isIncomplete())
                .collect(Collectors.toList());
    }

    /**
     * @return List of partial sleep entries.
     */
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<SleepEntry> getPartialSleepEntries() {
        return sleepEntries.stream()
                .filter(SleepEntry::isIncomplete)
                .collect(Collectors.toList());
    }

    /**
     * @return Get the global user models
     */
    @NonNull
    public ArrayList<User> getUserModels() {
        return userModels;
    }


    /**
     * @return Get a list of WeeklySleepHabit objects.
     */
    public List<WeeklySleepHabit> getWeeklySleepHabits() {
        return sleepModelsByWeeks;
    }

    /**
     * @return Get the global current user
     */
    @Nullable
    public User getCurrentUser() {
        return userModels.get(0);
    }

    /**
     * Get the Singleton instance
     * @return Singleton instance of GlobalData
     */
    @NonNull
    public static GlobalData getInstance() {
        return instance;
    }

    /**
     * Loads user and sleep models from database
     * @param db Open SQLite connection
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void update(DbConnection db) {
        // Get the user, if doesn't exist, create default
        instance.userModels = db.select(DbTables.user.TABLE_NAME, User.class, null, null);
        if (instance.userModels.size() == 0) {
            User user = new User("your name", 7 * DateTime.SECONDS_IN_HOUR,4);
            db.insert(user);
            instance.userModels = db.select(DbTables.user.TABLE_NAME, User.class, null, null);
        }

        // Get the sleep entries
        instance.sleepEntries = db.select(DbTables.sleep.TABLE_NAME, SleepEntry.class,
                null, null);

        Collections.reverse(instance.sleepEntries);
        instance.sleepModelsByWeeks = splitSleepEntriesToWeeks(instance.getCompletedSleepEntries());
        setClean();
    }

    /**
     * This handy function allows to use distinct with 2+ fields
     * https://howtodoinjava.com/java8/stream-distinct-by-multiple-fields/
     * @param extractors extractor field extractors
     * @param <T> type
     * @return true if item is distinct
     */
    @SafeVarargs
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... extractors) {
        final Map<List<?>, Boolean> saved = new HashMap<>();
        return t -> {
            final List<?> keys = Arrays.stream(extractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
            return saved.putIfAbsent(keys, true) == null;
        };
    }

    /**
     * Split sleep entries to weeks
     * @param entries all entries
     * @return List of weekly sleep habits
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<WeeklySleepHabit> splitSleepEntriesToWeeks(List<SleepEntry> entries) {
        Calendar calendar = Calendar.getInstance(Locale.GERMAN); // for the EU weekdays
        List<WeeklySleepHabit> results = entries.stream()
                .map(entry -> {
                    calendar.setTimeInMillis(DateTime.Unix.getMillis(entry.getStartTimestamp()));
                    int week = calendar.get(Calendar.WEEK_OF_YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    return new SimpleDate(week, month, year);
                })
                .filter(distinctByKeys(SimpleDate::getWeek, SimpleDate::getYear))
                .map(date -> new WeeklySleepHabit(date, new SleepEntry[7]))
                .collect(Collectors.toCollection(ArrayList::new));

        entries.forEach(sleep -> {
            calendar.setTimeInMillis(DateTime.Unix.getMillis(sleep.getStartTimestamp()));
            results.forEach(weeklyEntries -> {
                int year = weeklyEntries.getDate().getYear();
                int week = weeklyEntries.getDate().getWeek();
                boolean yearMatches = year == calendar.get(Calendar.YEAR);
                boolean monthMatch = week == calendar.get(Calendar.WEEK_OF_YEAR);

                if (yearMatches && monthMatch) {
                    DateTime.IndexedWeekDay day = DateTime.Unix.getWeekDay(sleep.getStartTimestamp());
                    weeklyEntries.getDays()[day.getIndex()] = sleep;
                }
            });
        });

        for (int i = 0; i < results.size(); i++)
            for (int j = 0; j < results.get(i).getDays().length; j++)
                if (results.get(i).getDays()[j] == null)
                    results.get(i).getDays()[j] = new SleepEntry();

        return results;
    }

    private GlobalData() {}

    /**
     * @return Is the GlobalData Singleton behind the database.
     */
    public static boolean isDirty() {
        return isDirty;
    }

    /**
     * Set GlobalData status clean.
     */
    private static void setClean() {
        isDirty = false;
    }

    /**
     * Set GlobalData satus dirty.
     */
    public static void setDirty() {
        isDirty = true;
    }
}
