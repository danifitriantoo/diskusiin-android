package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;

public class ProfileActivity extends AppCompatActivity {

    private Button btnLogout;
    private PreferencesHelper preferencesHelper;
    private TextView studentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferencesHelper = PreferencesHelper.getInstance(getApplicationContext());
        studentNumber = findViewById(R.id.studentNumber);

        studentNumber.setText(preferencesHelper.getToken());

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(
                view -> {
                    try {
                        preferencesHelper.clearCredentials();

                    } finally {
                        Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }
        );

    }
}