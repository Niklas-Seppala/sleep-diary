package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.sleepdiary.data.DBConnection;
import com.example.sleepdiary.data.SleepDBModel;
import com.example.sleepdiary.data.SleepRating;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DBConnection db = new DBConnection(this);

        SleepDBModel m = new SleepDBModel(666, SleepRating.OK,
                18239398,
                28394082);
        db.insert(m);

        ArrayList<SleepDBModel> models = db.select(SleepDBModel.TABLE_NAME, SleepDBModel.class,
                null, null);

        // LOG ALL DB ITEMS
        for (int i = 0; i < models.size(); i++) {
            Log.i("DB_ITEMS", "onCreate: " + models.get(i).toString());
        }

    }
}