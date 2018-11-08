package com.aidr.aidr;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class ReminderFragment extends Fragment {

    private TextView testText;
    private File file;
    final static public String filename = "reminders.json";
    public static JSONArray currReminders = null;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.reminder, container, false);
        checkIfRemindersExists();
        return rootView;
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