package com.danifitrianto.diskussin.setups.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.danifitrianto.diskussin.R;
import com.danifitrianto.diskussin.models.Rooms;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private Context context;
    private List<Rooms> models;
    private ItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvFacilityOne,tvStatus;
        public CardView cvRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvRoom = itemView.findViewById(R.id.cvRoom);
            this.tvName = itemView.findViewById(R.id.tvRoomName);
            this.tvFacilityOne = itemView.findViewById(R.id.tvFacilityOne);
            this.tvStatus = itemView.findViewById(R.id.tvFacilityTwo);
        }
    }

    public RoomAdapter(Context context, List<Rooms> models, ItemClickListener listener) {
        this.context = context;
        this.models = models;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rooms data = models.get(position);
        holder.tvName.setText(data.getName());
        holder.tvFacilityOne.setText(data.getFacility());
        holder.tvStatus.setText(String.valueOf(data.isStatus()));

        holder.cvRoom.setOnClickListener(view -> itemClickListener.onClick(data));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
