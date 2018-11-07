package com.aidr.aidr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminder extends AppCompatActivity {

    private Date selectedDate;
    private int tempYear;
    private int tempMonth;
    private int tempDay;
    public int tempHour;
    public int tempMinute;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd, yyyy");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private TextView chosenDateText;
    private TextView chosenTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder2);

        selectedDate = new Date();

        chosenDateText = ((TextView) findViewById(R.id.chosenDateView));
        String currentDateOut = sdfDate.format(selectedDate);
        chosenDateText.setText(currentDateOut);

        chosenTimeText = ((TextView) findViewById(R.id.chosenTimeView));
        String currentTimeOut = sdfTime.format(selectedDate);
        chosenTimeText.setText(currentTimeOut);
    }

    /* Save reminder */
    public void saveReminder(View view) {

        switchToMain(view);
    }

    // Close this activity -> return to main activity, which is AiDrChat
    public void switchToMain(View view) {
        finish();
    }

    // Pops up dialog to show time picker -> NOT DONE YET
    public void showTimePicker(View view) {
        final TimePickerFragment timeDialog = new TimePickerFragment();
        timeDialog.setParentActivity(this);
        timeDialog.show(getSupportFragmentManager(), "timePicker");
    }

    public void updateReminderTime() {
        Calendar c = Calendar.getInstance();
        c.set(tempYear,tempMonth,tempDay,tempHour,tempMinute);
        selectedDate = c.getTime();
        String timeOut = sdfTime.format(selectedDate);
        chosenTimeText.setText(timeOut);
    }

    // Pops up calendar picker dialog
    public void showCalendarPicker(View view) {
        final Dialog calDialog = new Dialog(view.getContext());
        calDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calDialog.setContentView(R.layout.calendar_picker);
        calDialog.setCanceledOnTouchOutside(true);
        calDialog.setCancelable(true);

        // Confirmation button
        Button confirm = (Button) calDialog.findViewById(R.id.confirmButton);

        // Set listener when user picks a date - saves the date temporarily
        ((CalendarView)calDialog.getWindow().findViewById(R.id.calendarView)).setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                tempYear = year;
                tempMonth = month;
                tempDay = dayOfMonth;
            }
        });

        // Set click listener for confirmation button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(tempYear,tempMonth,tempDay, tempHour, tempMinute);
                selectedDate = c.getTime();
                String dateOut = sdfDate.format(selectedDate);
                chosenDateText.setText(dateOut);
                calDialog.dismiss();
            }
        });

        calDialog.show();
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        public AddReminder parentActivity;
        private int tempHour;
        private int tempMinute;

        public void setParentActivity(AddReminder pa) {
            parentActivity = pa;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            tempHour = c.get(Calendar.HOUR_OF_DAY);
            tempMinute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(),this,tempHour,tempMinute,true);
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            tempHour = hour;
            tempMinute = minute;
            if (parentActivity != null) {
                parentActivity.tempHour = tempHour;
                parentActivity.tempMinute = tempMinute;
                parentActivity.updateReminderTime();
            }
        }

        public int getHour() {
            return tempHour;
        }

        public int getMinute() {
            return tempMinute;
        }

    }
}
