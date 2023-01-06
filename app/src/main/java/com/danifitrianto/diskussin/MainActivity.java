package com.danifitrianto.diskussin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.views.activities.HomepageActivity;
import com.danifitrianto.diskussin.views.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferencesHelper = PreferencesHelper.getInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                if(!preferencesHelper.getToken().isEmpty()) {
                    Intent i = new Intent(MainActivity.this, HomepageActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000); // wait for 5 seconds

    }
}