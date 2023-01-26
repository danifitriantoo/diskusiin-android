package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.Users;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.services.DecodeToken;
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
            }else {
                Toast.makeText(LoginActivity.this,
                        "Silahkan Masukan NIM dan Password anda!", Toast.LENGTH_SHORT).show();
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

                if(response.code() == 200) {

                    try {
                        JWT jwt = new JWT(response.body());
                        String clainNim = jwt.getClaim("email").asString();

                        preferencesHelper.setCredentials(
                                clainNim,
                                "Bearer " + response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(LoginActivity.this,HomepageActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "NIM atau Password Tidak Sesuai!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("failed",": falied" + t);
            }
        });

    }
}