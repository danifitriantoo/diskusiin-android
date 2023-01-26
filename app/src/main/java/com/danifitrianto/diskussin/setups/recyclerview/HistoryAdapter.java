package com.danifitrianto.diskussin.setups.recyclerview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.database.RentingsEntity;
import com.danifitrianto.diskussin.setups.prefs.PreferencesHelper;
import com.danifitrianto.diskussin.setups.retrofit.ApiClient;
import com.danifitrianto.diskussin.setups.retrofit.EndpointInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<Rentings> models;
    private List<RentingsEntity> localModels;
    private ItemClickListener itemClickListener;
    private PreferencesHelper preferencesHelper;

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNamaRuang,tvDetails,tvPending,tvApproved,tvDisapproved;
        public CardView cvRoom;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvRoom = itemView.findViewById(R.id.cvRoom);
            this.tvPending = itemView.findViewById(R.id.tvPending);
            this.tvApproved = itemView.findViewById(R.id.tvApproved);
            this.tvDisapproved = itemView.findViewById(R.id.tvDisapproved);
            this.tvNamaRuang = itemView.findViewById(R.id.tvNamaRuang);
            this.tvDetails = itemView.findViewById(R.id.tvDetais);
        }
    }

    public HistoryAdapter(Context context, List<Rentings> models, List<RentingsEntity> localModels, ItemClickListener listener) {
        preferencesHelper = PreferencesHelper.getInstance(context.getApplicationContext());
        this.context = context;
        this.models = models;
        this.localModels = localModels;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item,parent,false);
        return new HistoryAdapter.HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        if(models.size() > 0) {
            final Rentings data = models.get(position);
            EndpointInterface services = ApiClient.createService(
                    EndpointInterface.class,
                    preferencesHelper.getToken());

            Call<Rooms> get = services.getSingleRoom(data.getRoom_id());
            get.enqueue(new Callback<Rooms>() {
                @Override
                public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                    holder.tvNamaRuang.setText(String.valueOf(response.body().getName()));
                    holder.tvDetails.setText(data.getTanggal() + " | " + data.getJumlah_orang() + " Partisipan");

                    switch (data.getStatus()) {
                        case 1:
                            holder.tvDisapproved.setVisibility(View.GONE);
                            holder.tvApproved.setVisibility(View.GONE);
                            break;
                        case 2:
                            holder.tvPending.setVisibility(View.GONE);
                            holder.tvDisapproved.setVisibility(View.GONE);
                            break;
                        case 3:
                            holder.tvApproved.setVisibility(View.GONE);
                            holder.tvPending.setVisibility(View.GONE);
                            break;
                    }

                }

                @Override
                public void onFailure(Call<Rooms> call, Throwable t) {
                    Log.d("Failed to fetch ","" + t);
                }
            });
            holder.cvRoom.setOnClickListener(view -> itemClickListener.onClick(data));
        }

        if(localModels.size() > 0){
            final RentingsEntity localData = localModels.get(position);

            switch (localData.getRoomId()) {
                case 1:
                    holder.tvNamaRuang.setText("Mini Cinema");
                    break;
                case 2:
                    holder.tvNamaRuang.setText("Ruang A");
                    break;
                case 3:
                    holder.tvNamaRuang.setText("Ruang B");
                    break;
            }


            holder.tvDetails.setText(localData.getTanggal() + " | " + localData.getJumlahOrang() + " Partisipan");

            switch (localData.getStatus()) {
                case 1:
                    holder.tvDisapproved.setVisibility(View.GONE);
                    holder.tvApproved.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.tvPending.setVisibility(View.GONE);
                    holder.tvDisapproved.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.tvApproved.setVisibility(View.GONE);
                    holder.tvPending.setVisibility(View.GONE);
                    break;
            }
            holder.cvRoom.setOnClickListener(view -> itemClickListener.onClick(localData));
        }


    }

    @Override
    public int getItemCount() {
        return models.size() > 0
                ? models.size()
                : localModels.size();
    }
}
