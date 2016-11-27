package com.cs321.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver{

    private NotificationManager notificationManager;
    private boolean isNActive = false;
    private int nID = 42;

    @Override
    public void onReceive(Context context, Intent intent) {
        createReminder(context);
    }

    private void createReminder(Context context){
        PendingIntent reminder = PendingIntent.getActivity(context, 0,
                new Intent(context, ReminderActivity.class), 0);

        NotificationCompat.Builder nBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(context)
                        .setContentTitle("Complete" + ReminderActivity.priority + "priority task!")
                        .setContentText(ReminderActivity.text)
                        .setTicker("Reminder: Item requires attention!")
                        .setSmallIcon(R.drawable.alert);

        nBuilder.setAutoCancel(true);
        nBuilder.setContentIntent(reminder);
        nBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nID, nBuilder.build());
        isNActive = true;

    }
}
