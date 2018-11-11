package com.aidr.aidr;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationService extends BroadcastReceiver {

    public static String NOTIF_ID = "notif-id";
    public static String NOTIF_CONTENT = "notif";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        System.out.println("Notification job received!");

        int id = intent.getIntExtra(NOTIF_ID,1);
        Notification notifPayload = intent.getParcelableExtra(NOTIF_CONTENT);
        nm.notify(id,notifPayload);

        System.out.println("Notification fired!");
    }

    public static class NotificationDismissIntent extends IntentService {

        public NotificationDismissIntent() {
            super("NotificationDismissIntent");
        }

        @Override
        protected void onHandleIntent(Intent i) {
            int reminderId = i.getIntExtra(NOTIF_ID,-1);
            System.out.println("Dismissing notif #" + reminderId + "...");
            if (reminderId != -1) {
                ReminderDB.deleteReminderByPos(reminderId,true);
            }
        }
    }
}
