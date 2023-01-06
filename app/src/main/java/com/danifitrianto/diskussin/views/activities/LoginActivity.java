package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.Users;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etNim, etKey;
    private Button btnLogin;
    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferencesHelper = PreferencesHelper.getInstance(getApplicationContext());

        etNim = findViewById(R.id.etNim);
        etKey = findViewById(R.id.etKey);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {
            if(!etNim.getText().toString().isEmpty() && !etKey.getText().toString().isEmpty()) {
                authentication(etNim.getText().toString(),etKey.getText().toString());
            }
        });

    }

    private void authentication(String nim, String key) {
        Users model = new Users(nim,key);

        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<String> call = services.login(model);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Success","" + response.code());

                preferencesHelper.setCredentials(
                        model.getNim(),
                        model.getPassword(),
                        "Bearer " + response.body());

                if(preferencesHelper.getToken().length() > 0) {
                    Intent i = new Intent(LoginActivity.this,HomepageActivity.class);
                    startActivity(i);
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failed",": falied" + t);
            }
        });

    }
}