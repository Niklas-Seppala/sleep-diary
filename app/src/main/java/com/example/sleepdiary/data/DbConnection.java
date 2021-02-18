package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * SQLite database connection
 */
public class DbConnection extends SQLiteOpenHelper {

    /**
     * Creates a connection to SQLite database.
     * @param context app context
     */
    public DbConnection(@Nullable Context context) {
        super(context, "sleep.db", null, 1);
    }

    /**
     * Creates database if it doesn't exist.
     * @param sqLiteDatabase SQLite database object
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sb = new StringBuilder();
        sqLiteDatabase.execSQL(Db.sleep.createTableSQL(sb));
        sqLiteDatabase.execSQL(Db.user.createTableSQL(sb));
    }

    /**
     * Upgrades database if newer version.
     * @param sqLiteDatabase Database object
     * @param oldV old version number
     * @param newV new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) { }

    /**
     * Runs SELECT SQL statement, collects results to list.
     *
     * @param tableName Name of database table.
     * @param modelType Inserted model type.
     * @param sql SQL query as a String. Defaults to "SELECT *".
     * @param sArgs Array of selection arguments.
     * @param <T> Must implement DBModel
     * @return query result list.
     */
    public <T extends DbModel> ArrayList<T> select(String tableName, Class<T> modelType,
                                                   String sql, String[] sArgs)  {
        // If SQL string was provided use that
        String SQL_Statement = sql != null ? sql : "SELECT * FROM " + tableName;
        ArrayList<T> results = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_Statement, sArgs);
        if (cursor.moveToFirst()) {
            do {
                try {
                    // Instantiate object from Model Class, and deserialize
                    // values from current Cursor position.
                    results.add((T)modelType.getConstructor().newInstance().deserialize(cursor));
                } catch (Exception ignored)  { }
            } while (cursor.moveToNext()); // Move to new Cursor position
        }
        // Free Cursor resources
        cursor.close();
        return results;
    }

    /**
     * Inserts model object to database.
     * @param model Model object to be inserted.
     * @return true if insertion was succesful.
     */
    public boolean insert(DbModel model) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        model.serialize(cv);

        long affectedRows = SQLiteDB.insert(model.getTableName(), null, cv);
        return affectedRows > 0;
    }
}
