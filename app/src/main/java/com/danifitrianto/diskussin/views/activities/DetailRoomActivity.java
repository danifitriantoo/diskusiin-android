package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRoomActivity extends AppCompatActivity {

    private TextView tvTitleRoom,tdBoxDTanggal,tdBoxDWaktu,tdBoxDNama;
    private CheckBox cbAgree;
    private Button btnRent;
    private boolean isChecked = false;
    private PreferencesHelper preferencesHelper;
    private EditText etKeperluan,etTanggal,etMulai,etDurasi,etJumlahOrang;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        tvTitleRoom = findViewById(R.id.tvTitleRoom);
        preferencesHelper = PreferencesHelper.getInstance(getApplicationContext());

        cbAgree = findViewById(R.id.cbAgree);
        btnRent = findViewById(R.id.btnRent);
        etKeperluan = findViewById(R.id.etKeperluan);
        etTanggal = findViewById(R.id.etTanggal);
        etMulai = findViewById(R.id.etMulai);
        etDurasi = findViewById(R.id.etDurasi);
        etJumlahOrang = findViewById(R.id.etJumlahOrang);
        tdBoxDTanggal = findViewById(R.id.tdBoxDTangal);
        tdBoxDWaktu = findViewById(R.id.tdBoxDWaktu);
        tdBoxDNama = findViewById(R.id.tdBoxDNama);

        if(getIntent().getIntExtra("id_edit",0) > 0) {
            isEditMode = true;
            etKeperluan.setText(getIntent().getStringExtra("keperluan"));
            etTanggal.setText(getIntent().getStringExtra("tanggal"));
            etMulai.setText(getIntent().getStringExtra("waktu"));
            etDurasi.setText(String.valueOf(getIntent().getIntExtra("durasi",0)));
            etJumlahOrang.setText(getIntent().getStringExtra("jumlah_orang"));
            tdBoxDWaktu.setText(getIntent().getStringExtra("waktu"));
            tdBoxDTanggal.setText(getIntent().getStringExtra("tanggal"));
            tdBoxDNama.setText("John Doe");
            btnRent.setText("Ubah Detail Peminjaman");
        } else {
            tvTitleRoom.setText(getIntent().getStringExtra("name"));
        }

        cbAgree.setOnCheckedChangeListener((compoundButton, b) -> {
            isChecked = !isChecked;
            cbAgree.setChecked(isChecked);
        });

        btnRent.setOnClickListener(view -> {
            if(isChecked == true) {
                if (isEditMode == true) {

                    Rentings model = new Rentings(
                            getIntent().getIntExtra("id_edit", 0),
                            getIntent().getIntExtra("room_id_edit", 1),
                            21009090,
                            etKeperluan.getText().toString(),
                            etTanggal.getText().toString(),
                            etMulai.getText().toString(),
                            Integer.parseInt(etDurasi.getText().toString()),
                            Integer.parseInt(etJumlahOrang.getText().toString()),
                            1);

                    editData(model);
                } else {
                    Rentings model = new Rentings(0,
                            getIntent().getIntExtra("id", 0),
                            21009090,
                            etKeperluan.getText().toString(),
                            etTanggal.getText().toString(),
                            etMulai.getText().toString(),
                            Integer.parseInt(etDurasi.getText().toString()),
                            Integer.parseInt(etJumlahOrang.getText().toString()),
                            1);

                    postData(model);
                }
            }
        });

    }

    private void postData(Rentings rentings) {
        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<Rentings> call = services.rentRoom(rentings);
        call.enqueue(new Callback<Rentings>() {
            @Override
            public void onResponse(Call<Rentings> call, Response<Rentings> response) {
                if(response.code() == 201) {
                    class InsertDataAsync extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {

                            RentingsEntity entity = new RentingsEntity(response.body().getId(),
                                    1,
                                    response.body().getRoom_id(),
                                    response.body().getKeperluan(),
                                    response.body().getTanggal(),
                                    response.body().getWaktu(),
                                    response.body().getDurasi(),
                                    response.body().getJumlah_orang(),
                                    response.body().getStatus());

                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                    .rentingDao()
                                    .insert(entity);

                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finish();
                            Intent i = new Intent(DetailRoomActivity.this, ConfirmationActivity.class);
                            i.putExtra("id",response.body().getId());
                            i.putExtra("waktu",response.body().getWaktu());
                            i.putExtra("tanggal",response.body().getTanggal());
                            i.putExtra("keperluan",response.body().getKeperluan());
                            i.putExtra("jumlah_orang",String.valueOf(response.body().getJumlah_orang()));
                            i.putExtra("status",response.body().getStatus());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }

                    InsertDataAsync insert = new InsertDataAsync();
                    insert.execute();
                }
            }

            @Override
            public void onFailure(Call<Rentings> call, Throwable t) {
                Log.d("Failed", "" + t);
            }
        });
    }

    private void editData(Rentings rentings) {
        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<Rentings> call = services.editRent(rentings,rentings.getId());
        call.enqueue(new Callback<Rentings>() {
            @Override
            public void onResponse(Call<Rentings> call, Response<Rentings> response) {
                if(response.code() == 201) {
                    class InsertDataAsync extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {

//                            RentingsEntity entity = new RentingsEntity(response.body().getId(),
//                                    getIntent().getIntExtra("room_id",0),
//                                    response.body().getRoom_id(),
//                                    response.body().getKeperluan(),
//                                    response.body().getTanggal(),
//                                    response.body().getWaktu(),
//                                    response.body().getDurasi(),
//                                    response.body().getJumlah_orang(),
//                                    response.body().getStatus());
//
//                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                                    .rentingDao()
//                                    .insert(entity);

                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finish();
                            Intent i = new Intent(DetailRoomActivity.this, ConfirmationActivity.class);
                            i.putExtra("id",response.body().getId());
                            i.putExtra("waktu",response.body().getWaktu());
                            i.putExtra("tanggal",response.body().getTanggal());
                            i.putExtra("jumlah_orang",String.valueOf(response.body().getJumlah_orang()));
                            i.putExtra("status",response.body().getStatus());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }

                    InsertDataAsync insert = new InsertDataAsync();
                    insert.execute();
                }
            }

            @Override
            public void onFailure(Call<Rentings> call, Throwable t) {
                Log.d("Failed", "" + t);
            }
        });
    }


}