package com.aidr.aidr;

import android.content.Context;
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
import android.widget.TextView;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import com.aidr.aidr.Adapter.ReminderAdapter;
import com.aidr.aidr.Model.Reminder;

import java.util.ArrayList;

public class ReminderFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private ArrayList<Reminder> reminders = new ArrayList<>();

    private TextView testText;
    private File file;
    final static public String filename = "reminders.json";
    public static JSONArray currReminders = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.reminder, container, false);
        checkIfRemindersExists();
        return rootView;
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
        refreshReminders();
    }

    @Override
    public void onResume() {
        super.onResume();
        testText = (TextView) getView().findViewById(R.id.testText);
        refreshReminders();
    }

    public void checkIfRemindersExists() {
        /* Check reminder file. If not exists, create an empty file */
        file = new File(getContext().getFilesDir(), filename);
        if (!file.exists()) {
            try {
                FileOutputStream ostream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                String toWrite = "[]";
                ostream.write(toWrite.getBytes(Charset.defaultCharset()));
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            file = new File(getContext().getFilesDir(), filename);
        }
    }

    public void getRemindersFromFile() {
        boolean success = true;
        FileInputStream istream;
        try {
            istream = getContext().openFileInput(filename);
        } catch (FileNotFoundException fnfe) {
            // should not happen
            istream = null;
            fnfe.printStackTrace();
        }

        if (istream != null) {
            byte[] buffer = new byte[(int)file.length()];
            try {
                istream.read(buffer);
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }

            if (success) {
                String inputStr = new String(buffer, Charset.defaultCharset());
                try {
                    currReminders = new JSONArray(inputStr);
                } catch (Exception e) {
                    currReminders = null;
                    e.printStackTrace();
                }
            }

        }
    }

    /* Reading reminder file */
    public void refreshReminders() {
        if (currReminders == null) {
            getRemindersFromFile();
        }
        String output = "Reminders : " + currReminders.length();
        testText.setText(output);
    }

}