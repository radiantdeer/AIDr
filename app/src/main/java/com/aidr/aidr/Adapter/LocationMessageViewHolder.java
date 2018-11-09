package com.aidr.aidr.Adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aidr.aidr.Model.Hospital;
import com.aidr.aidr.Model.Message;
import com.aidr.aidr.R;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;

public class LocationMessageViewHolder extends MessagesListAdapter.IncomingMessageViewHolder<Message> {

    private ViewGroup vg;
    public static ArrayList<Hospital> hospitals = new ArrayList<>();

    public LocationMessageViewHolder(View itemView) {
        super(itemView);
        vg = (ViewGroup) itemView;

        if (hospitals.size() == 0) {
            hospitals.add(new Hospital("Rumah Sakit Boromeus","Jalan Ir. H. Juanda No. 100",4.0,1721, 7));
            hospitals.add(new Hospital("Rumah Sakit Ginjal Habibie", "Jalan Tubagus Ismail No. 46", 5.0,895, 5));
            hospitals.add(new Hospital("Rumah Sakit Hasan Sadikin","Jalan Pasteur No. 38", 4.5,3624, 14));
        }
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        LocationAdapter la = new LocationAdapter(vg.getContext(),hospitals);

        RecyclerView rv = vg.findViewById(R.id.hospitalCardContainer);
        rv.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(la);
    }
}
