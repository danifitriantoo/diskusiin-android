package com.danifitrianto.diskussin.setups.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.danifitrianto.diskussin.views.activities.HomepageActivity;

public class StatusReceiver extends BroadcastReceiver {

    public StatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkStatus.getConnectivityStatusString(context);
        if(status.isEmpty()) {
            status="No Internet Connection";
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
