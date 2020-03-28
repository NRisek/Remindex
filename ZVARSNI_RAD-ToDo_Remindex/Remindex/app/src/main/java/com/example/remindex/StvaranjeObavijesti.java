package com.example.remindex;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class StvaranjeObavijesti extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Remindex";

    private NotificationManager mManager;

    public StvaranjeObavijesti(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel channell = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channell.enableLights(true);
        channell.enableVibration(true);
        channell.setLightColor(R.color.Plava);
        channell.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channell);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_smile);
    }
}