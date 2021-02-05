package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class DBConnection  extends SQLiteOpenHelper {

    public DBConnection(@Nullable Context context) {
        super(context, "sleep.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CreateSleepTable =
                     "CREATE TABLE "                +
                     SleepDBModel.TABLE_NAME        + " (" +
                     SleepDBModel.COLUMN_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     SleepDBModel.COLUMN_DURATION   + " REAL NOT NULL," +
                     SleepDBModel.COLUMN_QUALITY    + " INTEGER NOT NULL, " +
                     SleepDBModel.COLUMN_START_TIME + " INTEGER NOT NULL, " +
                     SleepDBModel.COLUMN_END_TIME   + " INTEGER NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CreateSleepTable);
    }

    // Unused
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
                try { // Relax, libraries pull of this shit all the time.
                    Constructor<T> ctor = modelType.getConstructor();
                    T model = (T)ctor.newInstance().deserialize(cursor);
                    results.add(model);
                } catch (Exception ignored)  { }
            } while (cursor.moveToNext());
        }
        return results;
    }


    public boolean insert(DBModel model) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        model.serialize(cv);

        long affectedRows = SQLiteDB.insert(model.getTableName(), null, cv);
        return affectedRows > 0;
    }
}
