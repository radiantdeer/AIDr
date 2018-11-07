package com.aidr.aidr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidr.aidr.Adapter.ReminderAdapter;
import com.aidr.aidr.Model.Reminder;

import java.util.ArrayList;

public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private ArrayList<Reminder> reminders = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Add dummy data for experimental
        for (int i = 0; i < 5; i++){
            reminders.add(new Reminder("Medicine Title", "50 mg orally as needed for pain"));
        }

        //assign ReminderAdapter
        reminderAdapter = new ReminderAdapter(getContext(), reminders);

        //use findViewById for reference to xml view
        recyclerView = view.findViewById(R.id.rv_reminder_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reminderAdapter);

        //retrieve from database
        retrieveAllReminders();
    }

    private void retrieveAllReminders() {
        //this method will be implemented soon
    }
}