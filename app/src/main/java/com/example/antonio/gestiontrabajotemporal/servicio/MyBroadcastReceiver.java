package com.example.antonio.gestiontrabajotemporal.servicio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;

/**
 * Nuestro BroadcastReceiver se configurará para responder a una determinada acción del sistema,
 * y el propio sistema será el encargado de notificar al BroadcastReceiver de que ha sucedido dicha acción.
 * Notificamos a nuesto BroadcastReceiver cuando el sistema ha completado el proceso de arranque,
 * registrándolo al ACTION_BOOT_COMPLETED en el manifest.xml.
 *
 * Iniciamos el servicio.
 */
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