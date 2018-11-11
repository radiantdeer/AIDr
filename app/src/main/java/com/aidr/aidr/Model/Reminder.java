package com.aidr.aidr.Model;

import com.aidr.aidr.ReminderDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reminder {
    private String title;
    private String dosage;
    private String additionalNotes;
    final static public SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    private Date date;

    public Reminder() {
        title = "Sample Reminder";
        dosage = "Sample Dosage";
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,Calendar.HOUR_OF_DAY + 1, Calendar.MINUTE);
        date = temp.getTime();
        additionalNotes = "";
    }

    public Reminder(String title, String dosage, Date dueDate) {
        this.title = title;
        this.dosage = dosage;
        this.date = dueDate;
        this.additionalNotes = "";
    }

    public Reminder(String title, String dosage, Date dueDate, String additionalNotes) {
        this.title = title;
        this.dosage = dosage;
        this.date = dueDate;
        this.additionalNotes = additionalNotes;
    }

    public Reminder(JSONObject in) throws JSONException, ParseException {
        this.title = in.getString("title");
        this.dosage = in.getString("dosage");
        this.date = Reminder.sdf.parse(in.getString("dueDate"));
        this.additionalNotes = in.getString("additional");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{\n" +
                "   \"title\" : \"" + title + "\",\n" +
                "   \"dosage\" : \"" + dosage + "\",\n" +
                "   \"dueDate\" : \"" + sdf.format(date) + "\",\n" +
                "   \"additional\" : \"" + additionalNotes + "\"\n" +
                "}\n";
    }
}
