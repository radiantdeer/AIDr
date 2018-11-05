package com.aidr.aidr;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminder extends AppCompatActivity {

    private Date selectedDate;
    private int tempYear;
    private int tempMonth;
    private int tempDay;
    private SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
    private TextView chosenDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder2);

        chosenDateText = ((TextView) findViewById(R.id.chosenDateView));
        String currentDateOut = sdf.format(new Date());
        chosenDateText.setText(currentDateOut);

    }

    public void saveReminder(View view) {

        switchToMain(view);
    }

    public void switchToMain(View view) {
        finish();
    }

    public void showTimePicker(View view) {
        final Dialog timeDialog = new Dialog(view.getContext());
        timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeDialog.setContentView(R.layout.time_picker);
        timeDialog.setCanceledOnTouchOutside(false);
        timeDialog.setCancelable(true);
    }

    public void showCalendarPicker(View view) {
        final Dialog calDialog = new Dialog(view.getContext());
        calDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calDialog.setContentView(R.layout.calendar_picker);
        calDialog.setCanceledOnTouchOutside(true);
        calDialog.setCancelable(true);

        Button confirm = (Button) calDialog.findViewById(R.id.confirmButton);

        ((CalendarView)calDialog.getWindow().findViewById(R.id.calendarView)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                tempYear = year;
                tempMonth = month;
                tempDay = dayOfMonth;
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(tempYear,tempMonth,tempDay);
                selectedDate = c.getTime();
                String dateOut = sdf.format(selectedDate);
                chosenDateText.setText(dateOut);
                calDialog.dismiss();
            }
        });

        calDialog.show();
    }
}
