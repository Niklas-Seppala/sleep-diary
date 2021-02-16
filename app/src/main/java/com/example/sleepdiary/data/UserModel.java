package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public class UserModel implements DBModel {

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    private String name;
    private int id;

    public UserModel() {}
    public UserModel(String name) {
        this.id = -1;
        this.name = name;
    }

    @Override
    public DBModel serialize(ContentValues toRow) {
        toRow.put(COLUMN_NAME, this.name);
        return this;
    }

    @Override
    public DBModel deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case COLUMN_NAME:
                    this.name = fromRow.getString(i);
                    break;
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
}
