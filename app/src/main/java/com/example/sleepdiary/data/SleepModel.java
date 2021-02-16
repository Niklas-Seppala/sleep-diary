package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public class SleepModel implements DBModel {
    public static final String TABLE_NAME = "sleep";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_END_TIME = "end_timestamp";
    public static final String COLUMN_START_TIME = "start_timestamp";
    public static final String COLUMN_QUALITY = "quality";

    private int id;
    private int user_id;
    private SleepRating qualityRating;
    private int startTimestamp;
    private int endTimestamp;

    public SleepModel() { };
    public SleepModel(int user_id, int duration, SleepRating qualityRating,
                      int startTimestamp, int endTimestamp) {
        this.id = -1;
        this.user_id = user_id;
        this.qualityRating = qualityRating;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    public SleepModel serialize(ContentValues toRow) {
        toRow.put(COLUMN_USER_ID, this.user_id);
        toRow.put(COLUMN_START_TIME, this.startTimestamp);
        toRow.put(COLUMN_END_TIME, this.endTimestamp);
        toRow.put(COLUMN_QUALITY, this.qualityRating.toInt());
        return this;
    }

    public SleepModel deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {

            switch (fromRow.getColumnName(i)) {
                case COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case COLUMN_USER_ID:
                    this.user_id = fromRow.getInt(i);
                    break;
                case COLUMN_END_TIME:
                    this.endTimestamp = fromRow.getInt(i);
                    break;
                case COLUMN_START_TIME:
                    this.startTimestamp = fromRow.getInt(i);
                    break;
                case COLUMN_QUALITY:
                    this.qualityRating = SleepRating.fromInt(fromRow.getInt(i));
            }
        }
        return this;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public int getStartTimestamp() {
        return this.startTimestamp;
    }
    public int getEndTimestamp() {
        return this.endTimestamp;
    }
    public int getUserId() {
        return this.user_id;
    }
    public SleepRating getQualityRating() {
        return this.qualityRating;
    }
    public long getId() {
        return this.id;
    }
}