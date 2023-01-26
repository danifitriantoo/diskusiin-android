package com.danifitrianto.diskussin.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.recyclerview.HistoryAdapter;
import com.danifitrianto.diskussin.setups.recyclerview.ItemClickListener;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;
import com.danifitrianto.diskussin.views.activities.ConfirmationActivity;
import com.danifitrianto.diskussin.views.activities.HomepageActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private HistoryAdapter rvAdapter;
    private ItemClickListener rvListener;
    private LinearLayoutCompat linearLayoutCompat;
    private PreferencesHelper preferencesHelper;
    private List<RentingsEntity> models;
    private TextView tvWarning;
    private BroadcastReceiver checkNetwork;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rv_history);
        tvWarning = view.findViewById(R.id.tvWarning);
        preferencesHelper = PreferencesHelper.getInstance(getContext());
        linearLayoutCompat = view.findViewById(R.id.linearLayoutCompat);

        HomepageActivity parent = (HomepageActivity) getActivity();
        boolean isConnected = parent.getNetworkStatus();

        if(isConnected) {
            consumeData();
            tvWarning.setVisibility(View.GONE);
        } else {
            fetchDataFromRoom();
            tvWarning.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void fetchDataFromRoom() {
        List<Rentings> apiModels = new ArrayList<>();

        class FetchLocalRentings extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                models = DatabaseClient.getInstance(getContext()).getAppDatabase()
                        .rentingDao()
                        .getAll();

                Log.d("Failed to fetch data "," " + models.get(0).getRoomId() + "and" + models.get(0).getId());
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                initRecyclerview(apiModels,models);
            }
        }

        FetchLocalRentings task = new FetchLocalRentings();
        task.execute();
    }

    private void consumeData() {
        List<RentingsEntity> localModels = new ArrayList<>();

        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<List<Rentings>> call = services.getRentings();
        call.enqueue(new Callback<List<Rentings>>() {
            @Override
            public void onResponse(Call<List<Rentings>> call, Response<List<Rentings>> response) {
                if(response.body().size() > 0) {
                    initRecyclerview(response.body(),localModels);
                    rvHistory.setVisibility(View.VISIBLE);
                    linearLayoutCompat.setVisibility(View.GONE);
                } else {
                    rvHistory.setVisibility(View.GONE);
                    linearLayoutCompat.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Rentings>> call, Throwable t) {
                Log.d("Failed to fetch data "," " + t);
            }
        });
    }
    private void initRecyclerview(List<Rentings> listRents, List<RentingsEntity> localRents) {

        if(listRents.size() > 0) {
            rvListener = (ItemClickListener<Rentings>) model -> {
                Intent i = new Intent(getContext(), ConfirmationActivity.class);
                i.putExtra("id",model.getId());
                i.putExtra("room_id",model.getRoom_id());
                i.putExtra("tanggal",model.getTanggal());
                i.putExtra("keperluan",model.getKeperluan());
                i.putExtra("waktu",model.getWaktu());
                i.putExtra("durasi",model.getDurasi());
                i.putExtra("jumlah_orang",String.valueOf(model.getJumlah_orang()));
                i.putExtra("status",model.getStatus());
                startActivity(i);
            };
        } else {
            rvListener = (ItemClickListener<RentingsEntity>) localModel -> {
                Intent i = new Intent(getContext(), ConfirmationActivity.class);
                i.putExtra("id",localModel.getId());
                i.putExtra("room_id",localModel.getRoomId());
                i.putExtra("tanggal",localModel.getTanggal());
                i.putExtra("keperluan",localModel.getKeperluan());
                i.putExtra("waktu",localModel.getMulai());
                i.putExtra("durasi",localModel.getDurasi());
                i.putExtra("jumlah_orang",String.valueOf(localModel.getJumlahOrang()));
                i.putExtra("status",localModel.getStatus());
                startActivity(i);
            };
        }


        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new HistoryAdapter(getContext(), listRents,localRents,rvListener);
        rvHistory.setAdapter(rvAdapter);
    }
}