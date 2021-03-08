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
    private int caffeineIntake;

    public SleepEntry() { }

    public SleepEntry(SleepEntry partialEntry, int endTimestamp, Rating quality, int caffeineIntake) {
        this.id = partialEntry.getId();
        this.user_id = partialEntry.getUserId();
        this.startTimestamp = partialEntry.getStartTimestamp();
        this.quality = quality;
        this.caffeineIntake = caffeineIntake;
        this.endTimestamp = endTimestamp;
    }

    public SleepEntry(int user_id, int startTimestamp) {
        this.user_id = user_id;
        this.startTimestamp = startTimestamp;

        this.endTimestamp = -1;
        this.caffeineIntake = -1;
        this.quality = Rating.UNDEFINED;
    }

    public SleepEntry(int user_id, Rating quality, int startTimestamp,
                      int endTimestamp, int caffeineIntake) {
        this.id = -1;
        this.user_id = user_id;
        this.quality = quality;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.caffeineIntake = caffeineIntake;
    }

    public SleepEntry(int id, int user_id, Rating quality, int startTimestamp, // FIXME: DEV
                      int endTimestamp, int caffeineIntake) {
        this(user_id, quality, startTimestamp,
                endTimestamp, caffeineIntake);

        this.id = id;
    }

    @Override
    public SleepEntry serialize(ContentValues toRow) {
        toRow.put(DbTables.sleep.COLUMN_USER_ID, this.user_id);
        toRow.put(DbTables.sleep.COLUMN_START_TIME, this.startTimestamp);
        toRow.put(DbTables.sleep.COLUMN_END_TIME, this.endTimestamp);
        toRow.put(DbTables.sleep.COLUMN_QUALITY, this.quality.toInt());
        toRow.put(DbTables.sleep.COLUMN_CAFFEINE, this.caffeineIntake);
        return this;
    }

    @Override
    public SleepEntry deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {
            switch (fromRow.getColumnName(i)) {
                case DbTables.sleep.COLUMN_ID:
                    id = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_USER_ID:
                    user_id = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_END_TIME:
                    endTimestamp = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_START_TIME:
                    startTimestamp = fromRow.getInt(i);
                    break;
                case DbTables.sleep.COLUMN_QUALITY:
                    quality = Rating.fromInt(fromRow.getInt(i));
                    break;
                case DbTables.sleep.COLUMN_CAFFEINE:
                    caffeineIntake = fromRow.getInt(i);
                    break;
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
     * @return caffeine intake in coffee cups
     */
    public int getCaffeineIntake() {
        return caffeineIntake;
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
     * @return Is the entry inclomplete.
     */
    public boolean isIncomplete() {
        return this.endTimestamp < 0;
    }

    /**
     * @return Sleep instance id.
     */
    public int getId() {
        return this.id;
    }
}