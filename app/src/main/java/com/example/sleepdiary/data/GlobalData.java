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
    private static void populateMockData(DbConnection db) {
//        UserModel mockUser = new UserModel("Niklas");
//        db.insert(mockUser);
        UserModel mockUser =  db.select(Db.user.TABLE_NAME, UserModel.class, null, null).get(0);
        SleepModel model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613659403, 1613689403);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613659403, 1613689403);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.GOOD, 1613659403, 1613689403);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1613659403, 1613689403);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.BAD, 1613659403, 1615689403);
        db.insert(model);
        model = new SleepModel(mockUser.getId(), Rating.OK, 1613659403, 1613639403);
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
