package com.danifitrianto.diskussin.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.views.activities.ConfirmationActivity;
import com.danifitrianto.diskussin.views.activities.DetailRoomActivity;
import com.danifitrianto.diskussin.views.activities.HomepageActivity;

public class ApprovedFragment extends Fragment {

    private TextView tvKeperluan, tvTanggal, tvWaktu, tvJumlahOrang;
    private TextView tvBoxTanggal, tvBoxWaktu, tvBoxNama;
    private Button btnAEdit;

    public ApprovedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_approved, container, false);

        tvKeperluan = view.findViewById(R.id.tvAKeperluan);
        tvTanggal = view.findViewById(R.id.tvATanggal);
        tvWaktu = view.findViewById(R.id.tvAWaktu);
        tvJumlahOrang = view.findViewById(R.id.tvAJumlahOrang);
        tvBoxNama = view.findViewById(R.id.tvAAPeminjam);
        tvBoxTanggal = view.findViewById(R.id.tvAATanggal);
        tvBoxWaktu = view.findViewById(R.id.tvAAWaktu);
        btnAEdit = view.findViewById(R.id.btnAEdit);

        ConfirmationActivity parent = (ConfirmationActivity) getActivity();
        boolean isConnected = parent.getNetworkStatus();

        if(isConnected) {
            btnAEdit.setVisibility(View.VISIBLE);
        } else {
            btnAEdit.setVisibility(View.GONE);
        }

        btnAEdit.setOnClickListener(view1 -> {
            getActivity().finish();
            Intent i = new Intent(getContext(), DetailRoomActivity.class);
            i.putExtra("id_edit",getActivity().getIntent().getIntExtra("id",0));
            i.putExtra("room_id_edit",getActivity().getIntent().getIntExtra("room_id",0));
            i.putExtra("tanggal",getActivity().getIntent().getStringExtra("tanggal"));
            i.putExtra("keperluan",getActivity().getIntent().getStringExtra("keperluan"));
            i.putExtra("waktu",getActivity().getIntent().getStringExtra("waktu"));
            i.putExtra("durasi",getActivity().getIntent().getIntExtra("durasi",0));
            i.putExtra("jumlah_orang",getActivity().getIntent().getStringExtra("jumlah_orang"));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        getData();

        return view;
    }

    private void getData() {
        if(getActivity().getIntent().getIntExtra("id",0) > -1) {
            tvTanggal.setText(getActivity().getIntent().getStringExtra("tanggal"));
            tvKeperluan.setText(getActivity().getIntent().getStringExtra("keperluan"));
            tvWaktu.setText(getActivity().getIntent().getStringExtra("waktu"));
            tvJumlahOrang.setText(getActivity().getIntent().getStringExtra("jumlah_orang"));
            tvBoxWaktu.setText(getActivity().getIntent().getStringExtra("waktu"));
            tvBoxTanggal.setText(getActivity().getIntent().getStringExtra("tanggal"));
            tvBoxNama.setText("John Doe");
        }
    }
}