package com.aidr.aidr.Model;

public class Reminder {
    private String title;
    private String dosage;
    private String time;
    private String date;

    public Reminder(String title, String dosage) {
        this.title = title;
        this.dosage = dosage;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
