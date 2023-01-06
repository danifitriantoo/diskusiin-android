package com.danifitrianto.diskussin.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.database.RoomsEntity;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.recyclerview.ItemClickListener;
import com.danifitrianto.diskussin.setups.recyclerview.RoomAdapter;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;
import com.danifitrianto.diskussin.setups.room.DatabaseClient;
import com.danifitrianto.diskussin.views.activities.DetailRoomActivity;
import com.danifitrianto.diskussin.views.activities.ProfileActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvRoom;
    private RoomAdapter rvAdapter;
    private ItemClickListener rvListener;
    private PreferencesHelper preferencesHelper;
    private ImageButton btnProfile;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_home, container, false);

        rvRoom = view.findViewById(R.id.rvRoomList);
        btnProfile = view.findViewById(R.id.btnProfile);
        preferencesHelper = PreferencesHelper.getInstance(getContext());

        btnProfile.setOnClickListener(view1 -> goProfile());

        consumeData();

        return view;
    }


    private void consumeData() {
        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<List<Rooms>> call = services.getRooms();
        call.enqueue(new Callback<List<Rooms>>() {
            @Override
            public void onResponse(Call<List<Rooms>> call, Response<List<Rooms>> response) {
                initRecyclerview(response.body());
            }

            @Override
            public void onFailure(Call<List<Rooms>> call, Throwable t) {
                Log.d("Failed to fetch data "," " + t);
            }
        });


    }

    private void initRecyclerview(List<Rooms> listRoom) {

        rvListener = (ItemClickListener<Rooms>) model -> {
            Intent i = new Intent(getContext(), DetailRoomActivity.class);
            i.putExtra("id",model.getId());
            i.putExtra("name",model.getName());
            startActivity(i);
        };

        rvRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new RoomAdapter(getContext(), listRoom,rvListener);
        rvRoom.setAdapter(rvAdapter);
    }

    private void goProfile() {
        Intent i = new Intent(getContext(), ProfileActivity.class);
        startActivity(i);
    }
}