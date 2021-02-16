package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public interface DBModel {

    DBModel serialize(ContentValues toRow);

    DBModel deserialize(Cursor fromRow);

    String getTableName();
}
