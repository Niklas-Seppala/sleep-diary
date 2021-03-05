package com.example.sleepdiary.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.sleepdiary.data.db.DbTables;

/**
 * Sleep instance database model class.
 */
public class SleepEntry extends Model {
    private int id;
    private int user_id;
    private Rating quality;
    private int startTimestamp;
    private int endTimestamp;

    public SleepEntry() { }
    public SleepEntry(int user_id, Rating quality, int startTimestamp,
                      int endTimestamp) {
        this.id = -1;
        this.user_id = user_id;
        this.quality = quality;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public SleepEntry serialize(ContentValues toRow) {
        toRow.put(DbTables.sleep.COLUMN_USER_ID, this.user_id);
        toRow.put(DbTables.sleep.COLUMN_START_TIME, this.startTimestamp);
        toRow.put(DbTables.sleep.COLUMN_END_TIME, this.endTimestamp);
        toRow.put(DbTables.sleep.COLUMN_QUALITY, this.quality.toInt());
        return this;
    }

    @Override
    public SleepEntry deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case DbTables.sleep.COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_USER_ID:
                    this.user_id = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_END_TIME:
                    this.endTimestamp = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_START_TIME:
                    this.startTimestamp = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_QUALITY:
                    this.quality = Rating.fromInt(fromRow.getInt(i));
            }
        }
        return this;
    }

    @Override
    public String getTableName() {
        return DbTables.sleep.TABLE_NAME;
    }

    @Override
    public String getViewString() {
        return "Id: " + this.id;
    }

    /**
     * @return Sleep instance starting time as unix timestamp.
     */
    public int getStartTimestamp() {
        return this.startTimestamp;
    }

    /**
     * @return Sleep instance end time as unix timestamp.
     */
    public int getEndTimestamp() {
        return this.endTimestamp;
    }

    /**
     * @return Sleep instance user id.
     */
    public int getUserId() {
        return this.user_id;
    }

    /**
     * @return Sleep instance quality rating.
     */
    public Rating getQuality() {
        return this.quality;
    }

    /**
     * @return Sleep instance id.
     */
    public int getId() {
        return this.id;
    }
}