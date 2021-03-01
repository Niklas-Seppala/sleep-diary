package com.example.sleepdiary.data;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.sleepdiary.time.DateTime;

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
    private ArrayList<UserModel> userModels;
    private ArrayList<SleepModel> sleepModels;
    private List<WeeklySleepHabit> sleepModelsByWeeks;

    /**
     * @return Gets the global sleep models
     */
    @NonNull
    public ArrayList<SleepModel> getSleepModels() {
        return this.sleepModels;
    }

    /**
     * @return Get the global user models
     */
    @NonNull
    public ArrayList<UserModel> getUserModels() {
        return this.userModels;
    }

    /**
     * @return Get the global current user
     */
    @Nullable
    public UserModel getCurrentUser() {
        return this.userModels.get(0);
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
        instance.userModels = db.select(Db.user.TABLE_NAME, UserModel.class,
                null, null);
        instance.sleepModels = db.select(Db.sleep.TABLE_NAME, SleepModel.class,
                null, null);
        instance.sleepModelsByWeeks = splitSleepEntriesToWeeks(instance.sleepModels);

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
    private static List<WeeklySleepHabit> splitSleepEntriesToWeeks(List<SleepModel> models) {
        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        Collections.reverse(models);
        List<WeeklySleepHabit> results = models.stream()
                .map(sleep -> {
                    calendar.setTimeInMillis(DateTime.Unix.getMillis(sleep.getStartTimestamp()));
                    return new Pair<>(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR));
                })
                .filter(distinctByKeys(pair -> pair.first,
                        pair -> pair.second))
                .map(pair -> new WeeklySleepHabit(pair.first, pair.second, new SleepModel[7]))
                .collect(Collectors.toCollection(ArrayList::new));

        models.forEach(sleep -> {
            calendar.setTimeInMillis(DateTime.Unix.getMillis(sleep.getStartTimestamp()));
            results.forEach(week -> {
                if (week.getYear() == calendar.get(Calendar.YEAR) &&
                        week.getWeek() == calendar.get(Calendar.WEEK_OF_YEAR)) {
                    week.getDays()[DateTime.Unix.getWeekDay(sleep.getStartTimestamp()).getIndex()] = sleep;
                }
            });
        });

        for (int i = 0; i < results.size(); i++)
            for (int j = 0; j < results.get(i).getDays().length; j++)
                if (results.get(i).getDays()[j] == null)
                    results.get(i).getDays()[j] = new SleepModel();

        return results;
    }


    // TODO: Dev func
    public static void populateMockData(DbConnection db) {
        UserModel mockUser = new UserModel("Niklas", (int)(6.5f * DateTime.SECONDS_IN_HOUR));
        db.insert(mockUser);
        mockUser =  db.select(Db.user.TABLE_NAME, UserModel.class, null, null).get(0);
        SleepModel model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613684635, 1613712235);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613757955, 1613793955);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613847955, 1613894755);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1613941555, 1613973955);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.BAD, 1614027955, 1614056755);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1614113755, 1614141235);
        db.insert(model);

        model = new SleepModel(mockUser.getId(), Rating.OK, 1614543465, 1614562465);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1614631485, 1614653485);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1614710685, 1614726735);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1614797085, 1614832545);
        db.insert(model);
    }

    private GlobalData() { }

    public static boolean isDirty() {
        return isDirty;
    }

    protected static void setClean() {
        isDirty = false;
    }

    protected static void setDirty() {
        isDirty = true;
    }

    public List<WeeklySleepHabit> getSleepModelsByWeeks() {
        return sleepModelsByWeeks;
    }
}
