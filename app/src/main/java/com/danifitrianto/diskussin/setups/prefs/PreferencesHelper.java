package com.danifitrianto.diskussin.setups.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper  {
    private static final String PACKAGE_NAME ="com.danifitrianto.diskussin";
    private static PreferencesHelper INSTANCE;
    private SharedPreferences sharedPreferences;

    private PreferencesHelper(Context context) {
        sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PreferencesHelper(context);
        }
        return INSTANCE;
    }

    public SharedPreferences preferences() {
        return sharedPreferences;
    }

    public void setCredentials(String nim, String token)
    {
        sharedPreferences.edit().putString("nim", nim).apply();
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getNim() {
        return sharedPreferences.getString("nim", "");
    }
    public void clearCredentials() {
        sharedPreferences.edit().clear().apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }
}
