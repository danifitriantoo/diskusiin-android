package com.danifitrianto.diskussin.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;
import com.danifitrianto.diskussin.views.fragments.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRoomActivity extends AppCompatActivity {

    private TextView tvTitleRoom,tdBoxDTanggal,tdBoxDWaktu,tdBoxDNama;
    private CheckBox cbAgree;
    private Button btnRent;
    private boolean isChecked = false;
    private PreferencesHelper preferencesHelper;
    private AlertDialog.Builder builder;
    private EditText etKeperluan,etTanggal,etMulai,etDurasi,etJumlahOrang;
    private static final String channel = "com.danifitrianto.diskussin";
    private boolean isEditMode = false,networkStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        tvTitleRoom = findViewById(R.id.tvTitleRoom);
        preferencesHelper = PreferencesHelper.getInstance(getApplicationContext());
        createNotificationChannel();

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

            etTanggal.setEnabled(false);
            etMulai.setEnabled(false);
            etDurasi.setEnabled(false);

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
            if(isChecked) {
                builder = new AlertDialog.Builder(DetailRoomActivity.this);
                builder.setMessage(!isEditMode
                                ? "Apakah anda yakin meminjam ruang ini?"
                                : "Apakah anda yakin merubah detail pinjaman?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                if (isEditMode) {
                                    Rentings model = new Rentings(
                                            getIntent().getIntExtra("id_edit", 0),
                                            getIntent().getIntExtra("room_id_edit", 0),
                                            21009090,
                                            etKeperluan.getText().toString(),
                                            etTanggal.getText().toString(),
                                            etMulai.getText().toString(),
                                            Integer.parseInt(etDurasi.getText().toString()),
                                            Integer.parseInt(etJumlahOrang.getText().toString()),
                                            getIntent().getIntExtra("status",2));

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
                                            getIntent().getIntExtra("status",1));

                                    postData(model);
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Konfirmasi");
                alert.show();


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

                            RentingsEntity entity = new RentingsEntity(
                                    response.body().getId(),
                                    response.body().getRoom_id(),
                                    response.body().getUserId(),
                                    response.body().getKeperluan(),
                                    response.body().getTanggal(),
                                    response.body().getWaktu(),
                                    response.body().getDurasi(),
                                    response.body().getJumlah_orang(),
                                    response.body().getStatus());

                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                    .rentingDao()
                                    .insert(entity);

                            showNotification("peminjaman",String.valueOf(response.body().getRoom_id()));
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
                    class UpdateDataAsync extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                    .rentingDao()
                                    .update(getIntent().getIntExtra("id_edit",0),
                                            getIntent().getIntExtra("status",2),
                                            etKeperluan.getText().toString(),
                                            Integer.parseInt(etDurasi.getText().toString()),
                                            Integer.parseInt(etJumlahOrang.getText().toString())
                                            );

                            showNotification("perubahan data peminjaman",String.valueOf(2));
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finish();
                        }
                    }

                    UpdateDataAsync update = new UpdateDataAsync();
                    update.execute();
            }

            @Override
            public void onFailure(Call<Rentings> call, Throwable t) {
                Log.d("Failed", "" + t);
            }
        });
    }

    public void showNotification(String pesan,String ruang) {
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "com.danifitrianto.diskussin")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("PEMINJAMAN BERHASIL")
                .setContentText("Halo, " +  pesan + " anda terkait ruang diskusi " + ruang + " berhasil.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.danifitrianto.diskussin", "notification", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void openDatePicker(View view) {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                DetailRoomActivity.this,
                (view1, year1, monthOfYear, dayOfMonth)
                        -> etTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                year, month, day);
        datePickerDialog.show();

    }
}