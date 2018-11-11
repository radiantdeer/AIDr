package com.aidr.aidr.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidr.aidr.Model.Reminder;
import com.aidr.aidr.R;
import com.aidr.aidr.ReminderDB;
import com.aidr.aidr.ReminderFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
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
        private CardView mReminderCardView;

        Holder(@NonNull View itemView) {
            super(itemView);

            //use findViewById for reference to xml view
            mTitleTextView = itemView.findViewById(R.id.tv_title);
            mDosageTextView = itemView.findViewById(R.id.tv_dosage);
            mMedicineImageView = itemView.findViewById(R.id.iv_medicine_icon);
            mReminderCardView = itemView.findViewById(R.id.cv_reminder);

            mReminderCardView.setOnLongClickListener(reminderCardOnLongClick());
        }

        private View.OnLongClickListener reminderCardOnLongClick() {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.delete_reminder_title)
                            .setMessage(R.string.delete_reminder_message)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    /*
                                    ArrayList<JSONObject> list = new ArrayList<>();

                                    for (int j = 0; j < reminders.length(); j++) {
                                        try {
                                            list.add((JSONObject) reminders.get(j));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }*/

                                    // Assuming ReminderAdapter is listing reminders as-is
                                    ReminderDB.deleteReminderByPos(getAdapterPosition(),true);
                                    ReminderDB.cancelNotification(getAdapterPosition());

                                    /*list.remove(getAdapterPosition());
                                    ReminderFragment.currReminders = new JSONArray(list);
                                    reminders = new JSONArray(list);

                                    FileOutputStream ostream = null;
                                    try {
                                        ostream = context.openFileOutput(ReminderFragment.filename, Context.MODE_PRIVATE);
                                    } catch (Exception e) {
                                        // should not happen, file is guaranteed to be available
                                        e.printStackTrace();
                                    }

                                    if (ostream != null) {
                                        try {
                                            ostream.write(ReminderFragment.currReminders.toString().getBytes());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    */

                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                    return false;
                }
            };
        }
    }
}
