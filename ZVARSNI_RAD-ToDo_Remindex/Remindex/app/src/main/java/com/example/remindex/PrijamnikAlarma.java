package com.example.remindex;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;


public class PrijamnikAlarma extends BroadcastReceiver {
    PowerManager.WakeLock wakeLock;
    @Override
    public void onReceive(Context context, Intent intent) {

        String tekstDogadjaja=intent.getStringExtra("tekstDogađaja"); //NA MOBITELU SE NE CRAHA, A TU SE CRASHA
        StvaranjeObavijesti stvaranjeObavijesti = new StvaranjeObavijesti(context);
        NotificationCompat.Builder nb = stvaranjeObavijesti.getChannelNotification("Remindex", "Uskoro imate događaj: "+tekstDogadjaja); //NA MOBITELU SE NE CRAHA, A TU SE CRASHA
        stvaranjeObavijesti.getManager().notify(1, nb.build());
        //Ukoliko je uređaju ekren ugašen, WakeLock će ha "probuditi"
        //U manifest je dodano: <uses-permission android:name="android.permission.WAKE_LOCK" />
        //tag mora biti jednistven stoga moj počinje s "Remindex"

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ON_AFTER_RELEASE | ACQUIRE_CAUSES_WAKEUP, "Remindex:oznaka");
        wl.acquire(1000); //Pokreće WakeLock
    }

}
