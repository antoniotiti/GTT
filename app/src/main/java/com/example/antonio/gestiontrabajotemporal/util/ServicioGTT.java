package com.example.antonio.gestiontrabajotemporal.util;

import android.app.Service;
import android.content.Intent;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;

public class ServicioGTT extends Service {

    public static MainActivity UPDATE_LISTENER;
    private Handler handler;

    private static final String TAG = ServicioGTT.class.getSimpleName();

    public static void setUpdateListener(MainActivity mainActivity) {
        UPDATE_LISTENER = mainActivity;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();
        Log.d(TAG, "ServicioGTT creado...");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              //  UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };

    }
    @Override
    public int onStartCommand(Intent intent, int flags,int startId){
        Log.i("Local Service", "Received start id "+startId+": "+intent);
        Toast.makeText(this, "Local Service, Received start id "+startId+": "+intent , Toast.LENGTH_LONG).show();
        //Queremos que el servicio continúe ejecutándose hasta que es explícitamente parado, así que devolvemos sticky
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Servicio destruido", Toast.LENGTH_LONG).show();
        Log.d(TAG, "ServicioGTT destruido...");
    }


}