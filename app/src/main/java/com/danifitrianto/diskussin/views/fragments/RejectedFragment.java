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
import com.danifitrianto.diskussin.views.activities.HomepageActivity;

public class RejectedFragment extends Fragment {

    private TextView txtKeperluan, txtTanggal, txtWaktu, txtJumlahOrang;
    private Button btnHome;

    public RejectedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_rejected, container, false);

        txtKeperluan = view.findViewById(R.id.etKeperluan);
        txtTanggal = view.findViewById(R.id.txtDate);
        txtWaktu = view.findViewById(R.id.txtTime);
        txtJumlahOrang = view.findViewById(R.id.txtJumlahOrang);
        btnHome = view.findViewById(R.id.btnHomeReject);

        btnHome.setOnClickListener(view1 -> {
            getActivity().finish();
            Intent i = new Intent(getContext(), HomepageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        getData();

        return view;
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