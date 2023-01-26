package com.danifitrianto.diskussin.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;
import com.danifitrianto.diskussin.views.activities.ConfirmationActivity;
import com.danifitrianto.diskussin.views.activities.DetailRoomActivity;
import com.danifitrianto.diskussin.views.activities.HomepageActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment {

    private TextView txtKeperluan, txtTanggal, txtWaktu, txtJumlahOrang;
    private Button btnHome,btnCancel;
    private PreferencesHelper preferencesHelper;
    private AlertDialog.Builder builder;

    public PendingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_pending, container, false);

        txtKeperluan = view.findViewById(R.id.txtKeperluan);
        txtTanggal = view.findViewById(R.id.txtDate);
        txtWaktu = view.findViewById(R.id.txtTime);
        txtJumlahOrang = view.findViewById(R.id.txtJumlahOrang);

        preferencesHelper = PreferencesHelper.getInstance(getContext());
        btnHome = view.findViewById(R.id.btnHome);
        btnCancel = view.findViewById(R.id.btnCancel);

        ConfirmationActivity parent = (ConfirmationActivity) getActivity();
        boolean isConnected = parent.getNetworkStatus();

        if(isConnected) {
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
        }

        btnHome.setOnClickListener(view1 -> {
            getActivity().finish();
            Intent i = new Intent(getContext(), HomepageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda yakin membatalkan peminjaman?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteData(getActivity().getIntent().getIntExtra("id",0));
                                getActivity().finish();
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

        getData();

        return view;
    }

    private void deleteData(int id) {
        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<String> call = services.deleteRent(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                class DeleteDataAsync extends AsyncTask<Void, Void, Void> {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        DatabaseClient.getInstance(getContext()).getAppDatabase()
                                .rentingDao()
                                .deleteRent(id);
                        return null;
                    }
                }

                DeleteDataAsync task = new DeleteDataAsync();
                task.execute();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getData() {
        if(getActivity().getIntent().getIntExtra("id",0) > -1) {
            txtTanggal.setText(getActivity().getIntent().getStringExtra("tanggal"));
            txtKeperluan.setText(getActivity().getIntent().getStringExtra("keperluan"));
            txtWaktu.setText(getActivity().getIntent().getStringExtra("waktu"));
            txtJumlahOrang.setText(getActivity().getIntent().getStringExtra("jumlah_orang"));
        }
    }
}