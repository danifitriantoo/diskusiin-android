package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;

public class ConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        getData();
    }

    private void getData() {
        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .rentingDao()
                .getAll();
    }
}

