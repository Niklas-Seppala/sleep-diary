package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Database model for user table
 */
public class UserModel extends DbModel {
    private String name;
    private int id;

    public UserModel() {}
    public UserModel(String name) {
        this.id = -1;
        this.name = name;
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

    @Override
    public DbModel serialize(ContentValues toRow) {
        toRow.put(Db.user.COLUMN_NAME, this.name);
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
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return Db.user.TABLE_NAME;
    }
}
