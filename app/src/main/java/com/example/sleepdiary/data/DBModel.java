package com.example.sleepdiary.data;

import android.content.ContentValues;
import android.database.Cursor;

public interface DBModel {

    /**
     * Loads model's state to content values object.
     * @param dbValues Database content values.
     * @return Model instance.
     */
    DBModel serialize(ContentValues dbValues);

    /**
     * Deserializes database cursor values to this model object.
     * @param dbValues Cursor values.
     * @return Model instance.
     */
    DBModel deserialize(Cursor dbValues);

    /**
     * Get the model's corresponding table name.
     * @return model's table name.
     */

    String getTableName();
}
