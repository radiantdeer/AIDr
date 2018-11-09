package com.aidr.aidr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aidr.aidr.Model.Hospital;
import com.aidr.aidr.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Holder>{

    private Context context;
    private ArrayList<Hospital> hospitals;

    public LocationAdapter(Context context, ArrayList<Hospital> hospitals) {
        this.context = context;
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public LocationAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_hospital, viewGroup, false);
        return new LocationAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.Holder holder, int i) {
        holder.nameText.setText(hospitals.get(i).getName());
        holder.address.setText(hospitals.get(i).getAddress());
        holder.ratingText.setText(String.format("%.1f",hospitals.get(i).getRating()));
        holder.ratingBar.setRating((float) hospitals.get(i).getRating());
        holder.ratingCountText.setText(String.format("(%d)",hospitals.get(i).getNumRating()));
        holder.distanceText.setText(String.format("%.1f km", hospitals.get(i).getDistance()));
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public TextView nameText;
        public TextView address;
        public TextView ratingText;
        public RatingBar ratingBar;
        public TextView ratingCountText;
        public TextView distanceText;

        public Holder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.hospitalNameText);
            address = itemView.findViewById(R.id.hospitalAddressText);
            ratingText = itemView.findViewById(R.id.ratingText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingCountText = itemView.findViewById(R.id.ratingCountText);
            distanceText = itemView.findViewById(R.id.distanceText);
        }
    }
}
