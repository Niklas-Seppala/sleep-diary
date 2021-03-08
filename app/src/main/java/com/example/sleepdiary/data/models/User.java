package com.example.sleepdiary.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sleepdiary.data.db.DbTables;

/**
 * Database model for user table
 */
public class User extends Model {
    private String name;
    private int sleepGoal;
    private int caffeineGoal;
    private int id;

    public User() {} // NEEDED!
    public User(String name, int sleepGoal, int caffeineGoal) {
        this.id = -1;
        this.name = name;
        this.sleepGoal = sleepGoal;
        this.caffeineGoal = caffeineGoal;
    }
    public User(int id, String name, int sleepGoal, int caffeineGoal) {
        this(name, sleepGoal, caffeineGoal);
        this.id = id;
        this.caffeineGoal = caffeineGoal;
    }

    @Override
    public Model serialize(ContentValues toRow) {
        toRow.put(DbTables.user.COLUMN_NAME, name);
        toRow.put(DbTables.user.COLUMN_GOAL, sleepGoal);
        toRow.put(DbTables.user.COLUMN_CAFFEINE_GOAL, caffeineGoal);
        return this;
    }

    @Override
    public Model deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case DbTables.user.COLUMN_ID:
                    id = fromRow.getInt(i);
                    break;
                case DbTables.user.COLUMN_NAME:
                    name = fromRow.getString(i);
                    break;
                case DbTables.user.COLUMN_GOAL:
                    sleepGoal = fromRow.getInt(i);
                    break;
                case DbTables.user.COLUMN_CAFFEINE_GOAL:
                    caffeineGoal = fromRow.getInt(i);
                    break;
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return DbTables.user.TABLE_NAME;
    }

    @Override
    public String getViewString() {
        return "Id: " + this.id;
    }

    /**
     * @return User's set sleep duration goal in seconds.
     */
    public int getSleepGoal() {
        return sleepGoal;
    }

    /**
     * @return User id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Username
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return User's caffeine intake goal
     */
    public int getCaffeineGoal() {
        return caffeineGoal;
    }
}
