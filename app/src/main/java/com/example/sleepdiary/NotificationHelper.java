package com.example.sleepdiary;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

// THIS CLASS CREATES THE NOTIFICATION FOR THE ALARM AND SPECIFIES THE ANDROID VERSION.

// NotificationHelper method
public class NotificationHelper extends ContextWrapper {
    // Notification
    public static final String channel_id = "channel_id";
    public static final String channel_name = "channel_name";
    // ^ Visible to user!

    // NotificationManager variable
    private NotificationManager notificationManager;
    public NotificationHelper(Context base) {
        super(base);
        // Specifying the version currently running on this device.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    // NotificationManager Method
    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setContentTitle("Alarm!");
    }
}
