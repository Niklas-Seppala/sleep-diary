package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public class SleepDBModel implements DBModel {
    public static final String TABLE_NAME = "sleep";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_END_TIME = "end_timestamp";
    public static final String COLUMN_START_TIME = "start_timestamp";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_QUALITY = "quality";

    private long id;
    private double duration;
    private SleepRating qualityRating;
    private int startTimestamp;
    private int endTimestamp;

    public SleepDBModel() { };
    public SleepDBModel(double duration, SleepRating qualityRating,
                        int startTimestamp, int endTimestamp) {
        this.id = -1;
        this.duration = duration;
        this.qualityRating = qualityRating;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }

    @Override
    public SleepDBModel serialize(ContentValues toRow) {
        toRow.put(COLUMN_START_TIME, this.startTimestamp);
        toRow.put(COLUMN_END_TIME, this.endTimestamp);
        toRow.put(COLUMN_DURATION, this.duration);
        toRow.put(COLUMN_QUALITY, this.qualityRating.toInt());
        return this;
    }

    @Override
    public SleepDBModel deserialize(Cursor fromRow) {
        for (int i = 0; i < fromRow.getColumnCount(); i++) {

            switch (fromRow.getColumnName(i)) {
                case COLUMN_ID:
                    this.id = fromRow.getInt(i);
                    break;
                case COLUMN_DURATION:
                    this.duration = fromRow.getDouble(i);
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

    @Override
    public String toString() {
        return "SleepDBModel{" +
                "id=" + id +
                ", duration=" + duration +
                ", qualityRating=" + qualityRating +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                '}';
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public int getStartTimestamp() {
        return this.startTimestamp;
    }
    public int getEndTimestamp() {
        return this.endTimestamp;
    }
    public SleepRating getQualityRating() {
        return this.qualityRating;
    }
    public double getDuration() {
        return this.duration;
    }
    public long getId() {
        return this.id;
    }
}