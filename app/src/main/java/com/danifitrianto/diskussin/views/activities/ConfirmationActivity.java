package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private BroadcastReceiver checkNetwork;
    private IntentFilter filter;
    private boolean networkStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        checkNetwork = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                if(activeNetwork == null) {
                    networkStatus = false;
                } else {
                    networkStatus = true;
                }
            }
        };

        registerReceiver(checkNetwork,filter);

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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(checkNetwork,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(checkNetwork);
    }

    public boolean getNetworkStatus() {
        return networkStatus;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.confirmLayout,fragment);
        ft.commit();
    }
}