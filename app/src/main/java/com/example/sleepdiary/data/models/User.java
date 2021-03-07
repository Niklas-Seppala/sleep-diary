package com.example.sleepdiary.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sleepdiary.data.db.DbTables;

/**
 * Database model for user table
 */
public class User extends Model {
    private String name;
    private int goal;
    private int caffeine;
    private int id;

    public User() {} // NEEDED!
    public User(String name, int goal) {
        this.id = -1;
        this.name = name;
        this.goal = goal;
    }
    public User(int id, String name, int goal, int caffeine) {
        this(name, goal);
        this.id = id;
        this.caffeine = caffeine;
    }

    @Override
    public Model serialize(ContentValues toRow) {
        toRow.put(DbTables.user.COLUMN_NAME, this.name);
        toRow.put(DbTables.user.COLUMN_GOAL, this.goal);
        return this;
    }

    @Override
    public Model deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case DbTables.user.COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case DbTables.user.COLUMN_NAME:
                    this.name = fromRow.getString(i);
                    break;
                case DbTables.user.COLUMN_GOAL:
                    this.goal = fromRow.getInt(i);
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
    public int getGoal() {
        return goal;
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

}
