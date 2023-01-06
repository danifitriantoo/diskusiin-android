package com.danifitrianto.diskussin.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.recyclerview.HistoryAdapter;
import com.danifitrianto.diskussin.setups.recyclerview.ItemClickListener;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.views.activities.ConfirmationActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private HistoryAdapter rvAdapter;
    private ItemClickListener rvListener;
    private PreferencesHelper preferencesHelper;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rv_history);
        preferencesHelper = PreferencesHelper.getInstance(getContext());

        consumeData();

        return view;
    }

    private void consumeData() {
        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<List<Rentings>> call = services.getRentings();
        call.enqueue(new Callback<List<Rentings>>() {
            @Override
            public void onResponse(Call<List<Rentings>> call, Response<List<Rentings>> response) {
                initRecyclerview(response.body());
            }

            @Override
            public void onFailure(Call<List<Rentings>> call, Throwable t) {
                Log.d("Failed to fetch data "," " + t);
            }
        });
    }
    private void initRecyclerview(List<Rentings> listRents) {

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

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new HistoryAdapter(getContext(), listRents,rvListener);
        rvHistory.setAdapter(rvAdapter);
    }
}