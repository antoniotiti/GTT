package com.example.antonio.gestiontrabajotemporal.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context paramContext, Intent paramIntent) {

        Intent serviceIntent = new Intent();
        serviceIntent.setAction("com.example.antonio.gestiontrabajotemporal.util.MyService");
        paramContext.startService(serviceIntent);



        // LANZAR SERVICIO
        // paramContext.startService(new Intent(paramContext, ServicioGTT.class));
        // LANZAR SERVICIO

       /* if (Intent.ACTION_BOOT_COMPLETED.equals(paramIntent.getAction())) {
            paramContext.startService(new Intent(paramContext, ServicioGTT.class));
        }*/
    }
}