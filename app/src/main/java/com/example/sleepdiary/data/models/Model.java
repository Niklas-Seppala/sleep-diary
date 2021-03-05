package com.example.sleepdiary.data.models;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

/**
 * Database model that can serialize it's fields to
 * ContentValues object, and deserialize from Cursor object.
 */
public abstract class Model {
    /**
     * Loads model's state to content values object.
     * @param dbValues Database content values.
     * @return Model instance.
     */
    public abstract Model serialize(ContentValues dbValues);

    /**
     * Deserializes database cursor values to this model object.
     * @param dbValues Cursor values.
     * @return Model instance.
     */
    public abstract Model deserialize(Cursor dbValues);

    /**
     * Get the model's corresponding table name.
     * @return model's table name.
     */
    public abstract String getTableName();

    /**
     * Get the models "header string" for list views
     * @return model header
     */
    public abstract String getViewString();

    @NonNull
    @Override
    public String toString() {
        return  this.getViewString();
    }
}
