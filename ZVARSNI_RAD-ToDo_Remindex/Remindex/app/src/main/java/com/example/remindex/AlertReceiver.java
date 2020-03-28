package com.example.remindex;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;



public class AlertReceiver extends BroadcastReceiver {
    PowerManager.WakeLock wakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {

        String tekstDogadjaja=intent.getStringExtra("tekstDogađaja"); //NA MOBITELU SE NE CRAHA, A TU SE CRASHA
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Remindex", "Uskoro imate događaj: "+tekstDogadjaja); //NA MOBITELU SE NE CRAHA, A TU SE CRASHA
        notificationHelper.getManager().notify(1, nb.build());
        //Ukoliko je uređaju ekren ugašen, WakeLock će ha "probuditi"
        //U manifest je dodano: <uses-permission android:name="android.permission.WAKE_LOCK" />
        //tag mora biti jednistven stoga moj počinje s "Remindex"

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE, "Remindex:oznaka");
        wl.acquire(15000);
    }

}
