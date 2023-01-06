package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;
import com.danifitrianto.diskussin.views.fragments.ApprovedFragment;
import com.danifitrianto.diskussin.views.fragments.PendingFragment;
import com.danifitrianto.diskussin.views.fragments.RejectedFragment;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        switch (getIntent().getIntExtra("status",0)) {
            case 1 :
                loadFragment(new PendingFragment());
                break;
            case 2 :
                loadFragment(new ApprovedFragment());
                break;
            case 3 :
                loadFragment(new RejectedFragment());
                break;
            default:
                loadFragment(new PendingFragment());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.confirmLayout,fragment);
        ft.commit();
    }
}