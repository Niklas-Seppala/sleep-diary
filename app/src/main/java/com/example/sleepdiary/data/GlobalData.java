package com.example.sleepdiary.data;

import android.os.Build;
import android.util.Pair;

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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
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
     * @return Gets the global sleep models
     */
    @NonNull
    public ArrayList<SleepEntry> getSleepEntries() {
        return sleepEntries;
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
    } // FIXME ????

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
        instance.userModels = db.select(DbTables.user.TABLE_NAME, User.class, null, null);
        instance.sleepEntries = db.select(DbTables.sleep.TABLE_NAME, SleepEntry.class,
                null, null);
        instance.sleepModelsByWeeks = splitSleepEntriesToWeeks(instance.sleepEntries);
        setClean();
    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<WeeklySleepHabit> splitSleepEntriesToWeeks(List<SleepEntry> models) {
        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        Collections.reverse(models);
        List<WeeklySleepHabit> results = models.stream()
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

        models.forEach(sleep -> {
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

    private GlobalData() { }

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

    public static void __DEV__populateDb(DbConnection db, String username,
                                         double goal, int startTime, int sleepEntryCount) {
        final int MIN_SLEEP = 25000;
        final int MAX_SLEEP = 33000;
        final int MAX_DAYTIME = 70000;
        final int MIN_DAYTIME = 50000;

        User mockUser = new User(username, (int)(goal * DateTime.SECONDS_IN_HOUR));
        db.insert(mockUser);
        mockUser = db.select(DbTables.user.TABLE_NAME, User.class, null, null).get(0);

        int nextTime = startTime;
        for (int i = 0; i < sleepEntryCount; i++) {
            int sleepTime = nextTime;
            int nightTime = (int)((Math.random() * (MAX_SLEEP - MIN_SLEEP)) + MIN_SLEEP);
            nextTime += nightTime;
            int wakeTime = nextTime;
            int dayTime = (int)((Math.random() * (MAX_DAYTIME - MIN_DAYTIME)) + MIN_DAYTIME);
            nextTime += dayTime;
            SleepEntry entry = new SleepEntry(mockUser.getId(),
                    Rating.fromInt((int)(Math.random() * 4) + 1),
                    sleepTime,
                    wakeTime,
                    (int) (Math.random() * 6));
            db.insert(entry);
        }
    }
}
