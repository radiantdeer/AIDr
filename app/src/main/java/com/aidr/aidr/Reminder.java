package com.aidr.aidr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Reminder {

    private String title;
    private String dosage;
    private Date dueDate;
    final static private SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    private String additionalNotes;

    public Reminder() {
        title = "Sample Reminder";
        dosage = "Sample Dosage";
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,Calendar.HOUR_OF_DAY + 1, Calendar.MINUTE);
        dueDate = temp.getTime();
        additionalNotes = "";
    }

    public Reminder(String title, String dosage, Date dueDate) {
        this.title = title;
        this.dosage = dosage;
        this.dueDate = dueDate;
        this.additionalNotes = "";
    }

    public Reminder(String title, String dosage, Date dueDate, String additionalNotes) {
        this.title = title;
        this.dosage = dosage;
        this.dueDate = dueDate;
        this.additionalNotes = additionalNotes;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public long getMinuteLeft() {
        long remTime = dueDate.getTime() - (new Date()).getTime();
        remTime = TimeUnit.MILLISECONDS.toMinutes(remTime);
        return remTime;
    }

    @Override
    public String toString() {
        return "{\n" +
                "   \"title\" : \"" + title + "\",\n" +
                "   \"dosage\" : \"" + dosage + "\",\n" +
                "   \"dueDate\" : \"" + sdf.format(dueDate) + "\",\n" +
                "   \"additional\" : \"" + additionalNotes + "\"\n" +
                "}\n";
    }

}
