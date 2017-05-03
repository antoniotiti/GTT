package com.example.antonio.gestiontrabajotemporal.servicio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;
import com.example.antonio.gestiontrabajotemporal.ui.Registro;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
//TODO

/**
 * Nuestro BroadcastReceiver se configurará para responder a una determinada acción del sistema,
 * y el propio sistema será el encargado de notificar al BroadcastReceiver de que ha sucedido dicha acción.
 * Notificamos a nuesto BroadcastReceiver cuando el sistema ha completado el proceso de arranque,
 * registrándolo al ACTION_BOOT_COMPLETED en el manifest.xml.
 *
 * Iniciamos el servicio.
 */
public class MyReceiver extends BroadcastReceiver {

    public void onReceive(final Context paramContext, Intent paramIntent) {

        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {

               // Log.d(TAG, "FirstService started");
                // El servicio se finaliza a sí mismo cuando finaliza su
                // trabajo.
                // Simulamos trabajo de 10 segundos.
                //Thread.sleep(5000);

                // Instanciamos e inicializamos nuestro manager.
                NotificationManager nManager = (NotificationManager) paramContext.getSystemService(paramContext.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(paramContext)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("MyService")
                        .setContentText("Terminó el servicio!")
                        .setWhen(System.currentTimeMillis());

                // Creamos el Intent que llamará a nuestra Activity
                Intent targetIntent = new Intent(paramContext, MainActivity.class);

                // Creamos el PendingIntent
                PendingIntent contentIntent = PendingIntent.getActivity(
                        paramContext, 0, targetIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT); // FLAG_UPDATE_CURRENT indican el comportamiento del PendingIntent,
                // se actualizará la información del PendingIntent en el caso de que se lance uno y ya existiera previamente
                builder.setContentIntent(contentIntent);

                builder.setAutoCancel(true);

                nManager.notify(12345, builder.build());
                //Log.d(TAG, "sleep finished");
            }
        });
        hilo.start();
    }
}