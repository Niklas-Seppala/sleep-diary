package com.example.sleepdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.sleepdiary.data.DBConnection;
import com.example.sleepdiary.data.DBModel;
import com.example.sleepdiary.data.SleepModel;
import com.example.sleepdiary.data.SleepRating;
import com.example.sleepdiary.data.UserModel;

import java.util.ArrayList;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    DBConnection db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.db.close();
    }
}