package com.danifitrianto.diskussin.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.setups.services.StatusReceiver;
import com.danifitrianto.diskussin.views.fragments.HistoryFragment;
import com.danifitrianto.diskussin.views.fragments.HomeFragment;
import com.danifitrianto.diskussin.views.fragments.InformationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomepageActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private BroadcastReceiver StatusReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        StatusReceiver = new StatusReceiver();

        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                loadFragment(new HomeFragment());
                ;
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

//    private void statusBroadcast() {
//        registerReceiver(StatusReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//    }

}