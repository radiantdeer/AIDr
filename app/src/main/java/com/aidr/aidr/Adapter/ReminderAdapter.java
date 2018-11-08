package com.aidr.aidr.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidr.aidr.Model.Reminder;
import com.aidr.aidr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.Holder> {
    private Context context;
    private JSONArray reminders;

    public ReminderAdapter(Context context, JSONArray reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_reminder, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        String title = "";
        String dosage = "";
        try {
            JSONObject currentElement = (JSONObject) reminders.get(i);
            title = (String) currentElement.get("title");
            dosage = (String) currentElement.get("dosage");
        } catch (JSONException je) {
            // do nothing, this is guaranteed to work
        }

        holder.mTitleTextView.setText(title);
        holder.mDosageTextView.setText(dosage);
    }

    @Override
    public int getItemCount() {
        return reminders.length();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDosageTextView;
        private ImageView mMedicineImageView;

        Holder(@NonNull View itemView) {
            super(itemView);

            //use findViewById for reference to xml view
            mTitleTextView = itemView.findViewById(R.id.tv_title);
            mDosageTextView = itemView.findViewById(R.id.tv_dosage);
            mMedicineImageView = itemView.findViewById(R.id.iv_medicine_icon);
        }
    }
}
