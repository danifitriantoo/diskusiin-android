package com.danifitrianto.diskussin.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.views.fragments.HistoryFragment;
import com.danifitrianto.diskussin.views.fragments.HomeFragment;
import com.danifitrianto.diskussin.views.fragments.InformationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomepageActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private BroadcastReceiver checkNetwork;
    private LinearLayoutCompat linearLayoutCompat;
    private IntentFilter filter;
    private boolean networkStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

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

                loadFragment(new HomeFragment());
            }
        };

        registerReceiver(checkNetwork,filter);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                loadFragment(new HomeFragment());
                break;
            case R.id.history:
                loadFragment(new HistoryFragment());
                break;
            case R.id.information:
                loadFragment(new InformationFragment());
                break;
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout,fragment);
        ft.commit();
    }
}