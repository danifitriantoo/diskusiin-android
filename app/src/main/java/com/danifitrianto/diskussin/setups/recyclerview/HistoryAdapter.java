package com.danifitrianto.diskussin.setups.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.Rooms;
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
    private ItemClickListener itemClickListener;
    private PreferencesHelper preferencesHelper;

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNamaRuang,tvDetails;
        public CardView cvRoom;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvRoom = itemView.findViewById(R.id.cvRoom);
            this.tvNamaRuang = itemView.findViewById(R.id.tvNamaRuang);
            this.tvDetails = itemView.findViewById(R.id.tvDetais);
        }
    }

    public HistoryAdapter(Context context, List<Rentings> models, ItemClickListener listener) {
        preferencesHelper = PreferencesHelper.getInstance(context.getApplicationContext());
        this.context = context;
        this.models = models;
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

        final Rentings data = models.get(position);

        EndpointInterface services = ApiClient.createService(
                EndpointInterface.class,
                preferencesHelper.getToken());

        Call<Rooms> get = services.getSingleRoom(data.getRoom_id());
        get.enqueue(new Callback<Rooms>() {
            @Override
            public void onResponse(Call<Rooms> call, Response<Rooms> response) {
                holder.tvNamaRuang.setText(String.valueOf(response.body().getName()));
                holder.tvDetails.setText(data.getTanggal() + " | Jumlah Orang " + data.getJumlah_orang());
            }

            @Override
            public void onFailure(Call<Rooms> call, Throwable t) {
                Log.d("Failed to fetch ","" + t);
            }
        });

        holder.cvRoom.setOnClickListener(view -> itemClickListener.onClick(data));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
