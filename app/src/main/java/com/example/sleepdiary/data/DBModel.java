package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public interface DBModel {
    DBModel serialize(ContentValues dbValues);
    DBModel deserialize(Cursor dbValues);
    String getTableName();
}
