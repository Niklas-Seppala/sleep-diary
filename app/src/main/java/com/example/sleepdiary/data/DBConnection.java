package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class DBConnection extends SQLiteOpenHelper {

    public DBConnection(@Nullable Context context) {
        super(context, "sleep.db", null, 1);
    }

    /**
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE ").append(SleepModel.TABLE_NAME).append(" (")
            .append(SleepModel.COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append(SleepModel.COLUMN_USER_ID).append(" INTEGER NOT NULL,")
            .append(SleepModel.COLUMN_QUALITY).append(" INTEGER NOT NULL,")
            .append(SleepModel.COLUMN_START_TIME).append(" INTEGER NOT NULL,")
            .append(SleepModel.COLUMN_END_TIME).append(" INTEGER NOT NULL,")
            .append("FOREIGN KEY (").append(SleepModel.COLUMN_USER_ID).append(") REFERENCES ")
                .append(UserModel.TABLE_NAME).append("(").append(UserModel.COLUMN_ID).append(")")
            .append(");");
        String SQL_CreateSleepTable = sqlBuilder.toString();
        Log.i("SQL", SQL_CreateSleepTable);
        sqLiteDatabase.execSQL(SQL_CreateSleepTable);


        sqlBuilder.delete(0, sqlBuilder.length());
        sqlBuilder.append("CREATE TABLE ").append(UserModel.TABLE_NAME).append(" (")
            .append(UserModel.COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append(UserModel.COLUMN_NAME).append(" TEXT NOT NULL")
            .append(");");
        String SQL_CreateUserTable = sqlBuilder.toString();
        Log.i("SQL", SQL_CreateUserTable);
        sqLiteDatabase.execSQL(SQL_CreateUserTable);
    }

    /**
     *
     * @param sqLiteDatabase
     * @param oldV
     * @param newV
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) { }

    /**
     * Runs SELECT query, collects results to list.
     *
     * @param tableName Name of database table.
     * @param modelType Inserted model type.
     * @param sql SQL query as a String. Defaults to "SELECT *".
     * @param sArgs Array of selection arguments.
     * @param <T> Must implement DBModel
     * @return query result list.
     */
    public <T extends DBModel> ArrayList<T> select(String tableName, Class<T> modelType,
                                                   String sql, String[] sArgs) {
        String SQL_Statement = sql != null ? sql : "SELECT * FROM " + tableName;
        ArrayList<T> results = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_Statement, sArgs);

        if (cursor.moveToFirst()) {
            do {
                try {
                    Constructor<T> ctor = modelType.getConstructor();
                    T model = (T)ctor.newInstance().deserialize(cursor);
                    results.add(model);
                } catch (Exception ignored)  { }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    /**
     * Inserts model object to database.
     * @param model Model object to be inserted.
     * @return true if insertion was succesful.
     */
    public boolean insert(DBModel model) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        model.serialize(cv);

        long affectedRows = SQLiteDB.insert(model.getTableName(), null, cv);
        return affectedRows > 0;
    }
}
