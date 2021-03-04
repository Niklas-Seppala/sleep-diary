package com.example.sleepdiary.data.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepdiary.data.GlobalData;
import com.example.sleepdiary.data.models.Model;

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
        sqLiteDatabase.execSQL(DbTables.sleep.createTableSQL(sb));
        sqLiteDatabase.execSQL(DbTables.user.createTableSQL(sb));
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
    @SuppressLint("Assert")
    public <T extends Model> ArrayList<T> select(@NonNull String tableName,
                                                 @NonNull Class<T> modelType,
                                                 @Nullable String sql,
                                                 @Nullable String[] sArgs)  {
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
                } catch (Exception ex)  {
                    Log.e("MODEL", "select():" + ex.getMessage());

                    assert false;
                }
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
    public boolean insert(@NonNull Model model) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        model.serialize(contentValues);
        long affectedRows = SQLiteDB.insert(model.getTableName(), null,
                contentValues);
        GlobalData.setDirty();
        return affectedRows > 0;
    }

    /**
     * Executes DELETE SQL statement, with provided SQL WHERE clause.
     * @param tableName Name of the table where delete should be executed.
     * @param where SQL WHERE clause.
     * @param args Arguments for WHERE caluse.
     * @return true if deletion was succesful.
     */
    public boolean delete(@NonNull String tableName, // TODO: test this method
                          @NonNull String where,
                          @NonNull String[] args) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        long affectedRows = SQLiteDB.delete(tableName, where, args);

        GlobalData.setDirty();
        return affectedRows > 0;
    }

    /**
     * Executes UPDATE SQL statement, with provided SQL WHERE clause.
     * @param tableName Name of the table where update should be executed.
     * @param model Updated model.
     * @param where SQL where clause.
     * @param args Arguments for WHERE clause.
     * @return true if update was successful.
     */
    public boolean update(@NonNull String tableName, //TODO: test this method
                          @NonNull Model model,
                          @NonNull String where,
                          @NonNull String[] args) {
        SQLiteDatabase SQLiteDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        model.serialize(contentValues);
        long affectedRows = SQLiteDB.update(tableName, contentValues, where, args);

        GlobalData.setDirty();
        return affectedRows > 0;
    }

}
