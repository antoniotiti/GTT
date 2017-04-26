package com.example.antonio.gestiontrabajotemporal.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;
import com.example.antonio.gestiontrabajotemporal.sqlite.OperacionesBaseDatos;
import com.example.antonio.gestiontrabajotemporal.ui.MainActivity;

import java.text.ParseException;
import java.util.Calendar;

import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_fecha;
import static com.example.antonio.gestiontrabajotemporal.util.Utilidades.formatter_hora_minutos;

public class ServicioGTT extends Service {

    private static final String TAG = ServicioGTT.class.getSimpleName();
    public static MainActivity UPDATE_LISTENER;
    private Handler handler;
    OperacionesBaseDatos datos;
    SharedPreferences sharedPref;

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
        //Obtenemos las preferencias.
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        datos = OperacionesBaseDatos.obtenerInstancia(this);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //  UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String operarioActivo = sharedPref.getString("pref_operario_activo", "");
        String calendarioPredeterminado = sharedPref.getString("pref_calendario_predeterminado", "");
        Calendar cal = Calendar.getInstance();
        String fecha = formatter_fecha.format(cal.getTime());
        Cursor cursorIdCalendario = datos.obtenerIdCalendarioByNombre(calendarioPredeterminado);//Obtenemos el id del calendario
        //Nos aseguramos de que existe al menos un registro de calendario
        if (cursorIdCalendario.moveToFirst()) {
            String idCalendario = cursorIdCalendario.getString(0);
            Cursor cursorAlarma = datos.obtenerAlarmaFichaje(operarioActivo, idCalendario, fecha);
            Cursor cursorModoTelefono= datos.obtenerModoTelefonoFichaje(operarioActivo, idCalendario, fecha);

            //Nos aseguramos de que existe al menos un registro
            if (cursorAlarma.moveToFirst()) {
                int avisoAlarma = cursorAlarma.getInt(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Turnos.AVISO));

                if (avisoAlarma == 1) {
                    String horasAlarma = cursorAlarma.getString(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_AVISO));
                    String nombreTurno = cursorAlarma.getString(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
                    String horaInicioTurno = cursorAlarma.getString(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_INICIO_1));
                    String comentario = cursorAlarma.getString(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Fichajes.COMENTARIO));
                    String nombrePuesto = cursorAlarma.getString(cursorAlarma.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
                    Calendar alarmaHora = Calendar.getInstance();
                    try {
                        alarmaHora.setTime(formatter_hora_minutos.parse(horasAlarma));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // Schedule the alarm!
                    AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);


                    Intent alarmIntent = new Intent(this, MyReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

                    am.set(AlarmManager.RTC_WAKEUP, alarmaHora.getTimeInMillis(), pendingIntent);

                }
            }

            //Nos aseguramos de que existe al menos un registro
            if (cursorModoTelefono.moveToFirst()) {
                String modoTelefono = cursorModoTelefono.getString(cursorModoTelefono.getColumnIndex(NombresColumnasBaseDatos.Turnos.MODO_TELEFONO));

                Calendar alarmaHora = Calendar.getInstance();
                String modo=modoTelefono;
            }
        }


        //this.stopSelf();
        Log.i("Local Service", "Received start id " + startId + ": " + intent);
        Toast.makeText(this, "Tareas del servicio", Toast.LENGTH_LONG).show();
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

/*Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "FirstService started");
                // El servicio se finaliza a sí mismo cuando finaliza su
                // trabajo.
                try {
                    // Simulamos trabajo de 10 segundos.
                    Thread.sleep(5000);

                    // Instanciamos e inicializamos nuestro manager.
                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("MyService")
                            .setContentText("Terminó el servicio!")
                            .setWhen(System.currentTimeMillis());

                    // Creamos el Intent que llamará a nuestra Activity
                    Intent targetIntent = new Intent(getBaseContext(), Registro.class);

                    // Creamos el PendingIntent
                    PendingIntent contentIntent = PendingIntent.getActivity(
                            getBaseContext(), 0, targetIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT); // FLAG_UPDATE_CURRENT indican el comportamiento del PendingIntent,
                    // se actualizará la información del PendingIntent en el caso de que se lance uno y ya existiera previamente
                    builder.setContentIntent(contentIntent);

                    builder.setAutoCancel(true);

                    nManager.notify(12345, builder.build());
                    Log.d(TAG, "sleep finished");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        hilo.start();*/