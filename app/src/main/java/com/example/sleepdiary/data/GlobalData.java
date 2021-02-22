package com.example.sleepdiary.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
    public static void update(DbConnection db) {
        instance.userModels = db.select(Db.user.TABLE_NAME, UserModel.class,
                null, null);
        instance.sleepModels = db.select(Db.sleep.TABLE_NAME, SleepModel.class,
                null, null);
        setClean();
    }


    // TODO: Dev func
    public static void populateMockData(DbConnection db) {
        UserModel mockUser = new UserModel("Niklas");
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
}
