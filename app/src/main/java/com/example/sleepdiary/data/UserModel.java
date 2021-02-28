package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Database model for user table
 */
public class UserModel extends DbModel {
    private String name;
    private int goal;
    private int id;

    public UserModel() {
//        this.goal = 6 * TimeUtility.SECONDS_IN_HOUR; // FIXME: TEMPORARY
    }
    public UserModel(String name, int goal) {
        this.id = -1;
        this.name = name;
        this.goal = goal;
    }

    @Override
    public DbModel serialize(ContentValues toRow) {
        toRow.put(Db.user.COLUMN_NAME, this.name);
        toRow.put(Db.user.COLUMN_GOAL, this.goal);
        return this;
    }

    @Override
    public DbModel deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case Db.user.COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case Db.user.COLUMN_NAME:
                    this.name = fromRow.getString(i);
                    break;
                case Db.user.COLUMN_GOAL:
                    this.goal = fromRow.getInt(i);
                    break;
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return Db.user.TABLE_NAME;
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
