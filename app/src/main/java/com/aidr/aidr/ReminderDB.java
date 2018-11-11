package com.aidr.aidr;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.aidr.aidr.Model.Reminder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Date;

public class ReminderDB {

    private static String reminderFilename = "reminders.json";
    public static JSONArray currReminders = new JSONArray();
    private static Context appContext;
    private static File file;

    /* private static ReminderDB() {

       } */

    private static void checkIfFileExists() {
        file = new File(appContext.getFilesDir(), reminderFilename);
        if (!file.exists()) {
            try {
                FileOutputStream ostream = appContext.openFileOutput(reminderFilename, Context.MODE_PRIVATE);
                String toWrite = "[]";
                ostream.write(toWrite.getBytes(Charset.defaultCharset()));
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            file = new File(appContext.getFilesDir(), reminderFilename);
        }
    }

    public static void initialize (Context context) {
        System.out.println("Initializing ReminderDB...");
        appContext = context;
        checkIfFileExists();
        reload();
    }

    public static void reload() {
        boolean success = true;
        FileInputStream istream;
        try {
            istream = appContext.openFileInput(reminderFilename);
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
                System.out.println(inputStr);
                try {
                    currReminders = new JSONArray(inputStr);
                } catch (Exception e) {
                    currReminders = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONArray getReminders() {
        return currReminders;
    }

    public static void addReminder(Reminder r, boolean shouldDumpToFile) {
        try {
            currReminders.put(new JSONObject(r.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (shouldDumpToFile) {
            updateFile();
        }
    }

    public static void updateFile() {
        FileOutputStream ostream = null;
        try {
            ostream = appContext.openFileOutput(ReminderFragment.filename, Context.MODE_PRIVATE);
            if (ostream != null) {
                try {
                    ostream.write(currReminders.toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // should not happen, file is guaranteed to be available
            e.printStackTrace();
        }

    }

    /* Delete r from currReminders. Pretty much do nothing is r is not in currReminders */
    public static void deleteReminder(Reminder r, boolean shouldUpdateFile) {
        int i;
        for (i = 0;i < currReminders.length();i++) {
            try {
                JSONObject temp = currReminders.getJSONObject(i);
                String title = temp.getString("title");
                String dosage = temp.getString("dosage");
                Date dueDate = Reminder.sdf.parse(temp.getString("dueDate"));
                if (r.getTitle().equals(title) && r.getDosage().equals(dosage) && r.getDosage().equals(dueDate)) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        deleteReminderByPos(i,shouldUpdateFile);
    }

    /* Delete reminders from currReminders. Pretty much do nothing is i is out of range */
    public static void deleteReminderByPos(int i, boolean shouldUpdateFile) {
        if (i < currReminders.length()) {
            currReminders.remove(i);
            if (shouldUpdateFile) {
                updateFile();
            }
        }
    }

    /* Create a notification
     * notifId : position of reminder in currReminders (in ReminderDB)
     * Ignore if reminder time < current time */
    public static void scheduleNotification(Reminder r, int notifId) {
        long fireInMillis = r.getDate().getTime() - (new Date()).getTime();
        if (fireInMillis < 0) {
            /* Build notification object */
            Notification.Builder builder = new Notification.Builder(appContext);
            builder.setContentTitle(r.getTitle());
            builder.setContentText(r.getDosage());
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            Notification notif = builder.build();

            /* Set action to delete reminder when notification is dismissed */
            Intent dismissIntent = new Intent(appContext, NotificationService.NotificationDismissIntent.class);
            dismissIntent.putExtra(NotificationService.NOTIF_ID,notifId);
            notif.deleteIntent = PendingIntent.getService(appContext,notifId,dismissIntent,0);

            /* Creating pending intent for use in AlarmManager */
            Intent notifIntent = new Intent(appContext,NotificationService.class);
            notifIntent.putExtra(NotificationService.NOTIF_ID,notifId);
            notifIntent.putExtra(NotificationService.NOTIF_CONTENT,notif);
            PendingIntent pi = PendingIntent.getBroadcast(appContext,notifId,notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);

            /* Create alarm event */
            AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(SystemClock.elapsedRealtime() + fireInMillis,null),pi);
            } else {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + fireInMillis,pi);
            }
        }
    }

    /* Cancels a pending notification
     * notifId : position of reminder in currReminders */
    public static void cancelNotification(int notifId) {
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent notifIntent = new Intent(appContext, NotificationService.class);
        PendingIntent pi = PendingIntent.getBroadcast(appContext,notifId,notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }
}
