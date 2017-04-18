/*
package com.example.antonio.gestiontrabajotemporal.util;

*/
/**
 * Created by Antonio on 27/3/17.
 *//*


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import java.util.GregorianCalendar;

public class Servicio extends Service {
    static PendingIntent alarma;
    static AlarmManager am;
    static AudioManager audio;
    static int año;
    static int cancelaAlarma;
    static Context contextoServicio;
    static int dia;
    static int diaAntes;
    static int hora;
    static int horaAlarma;
    static int idTurno;
    static boolean leyendoAlarmas;
    static int mes;
    static int minuto;
    static int minutoAlarma;
    static MediaPlayer mp;
    static int numeroAlarma;
    static int numeroCalendario;
    static int numeroCalendarioBucle;
    static String path;
    static long[] patronVibracion;
    static SharedPreferences pref;
    static String realizarAccion;
    static int requestCode;
    static int requestCode2;
    static IntentFilter s_intentFilter;
    static int segundos;
    static String sonido = "path/to/file.txt";
    static Uri sonidoDefecto;
    static int variableBT1;
    static int variableBT2;
    static int variableModo1;
    static int variableModo2;
    static int variableWifi1;
    static int variableWifi2;
    static Vibrator vibrador;

    static {
        patronVibracion = new long[]{0L, 200L, 100L, 300L, 400L};
        leyendoAlarmas = false;
        variableWifi1 = 0;
        variableModo1 = 0;
        variableBT1 = 0;
        variableWifi2 = 0;
        variableModo2 = 0;
        variableBT2 = 0;
        realizarAccion = "";
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        s_intentFilter.addAction("android.intent.action.TIME_SET");
        s_intentFilter.addAction("android.intent.action.DATE_CHANGED");
    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
            paramAnonymousContext = paramAnonymousIntent.getAction();
            if ((paramAnonymousContext.equals("android.intent.action.TIME_SET")) || (paramAnonymousContext.equals("android.intent.action.TIMEZONE_CHANGED")) || (paramAnonymousContext.equals("android.intent.action.DATE_CHANGED"))) {
                Servicio.leeAlarmas();
            }
        }
    };

    public static void leeAlarmas() {
        if (!leyendoAlarmas) {
            leyendoAlarmas = true;
            numeroCalendarioBucle = 1;
            if (numeroCalendarioBucle <= 10) {
                Object localObject2 = new GregorianCalendar();
                año = ((GregorianCalendar) localObject2).get(1);
                mes = ((GregorianCalendar) localObject2).get(2);
                dia = ((GregorianCalendar) localObject2).get(5);
                hora = ((GregorianCalendar) localObject2).get(11);
                minuto = ((GregorianCalendar) localObject2).get(12);
                segundos = ((GregorianCalendar) localObject2).get(13);
                long l1 = ((GregorianCalendar) localObject2).getTimeInMillis();
                int i2 = año * 10000 + mes * 100 + dia;
                Object localObject1 = "dbCal" + numeroCalendarioBucle;
                localObject1 = new BaseDeDatos(contextoServicio, (String) localObject1, null, BaseDeDatos.v_db);
                SQLiteDatabase localSQLiteDatabase = ((BaseDeDatos) localObject1).getWritableDatabase();
                diaAntes = 0;
                Object localObject3 = localSQLiteDatabase.rawQuery("SELECT fecha, turno1, turno2, alarma1, alarma2, alarma1T2, alarma2T2, notificacion, hora, sonido, notificacionDiaAntes, notificacion2, notificacion2DiaAntes, hora2, sonido2 FROM dias WHERE fecha = '" + i2 + "'", null);
                if (((Cursor) localObject3).moveToFirst()) {
                    int i1 = ((Cursor) localObject3).getInt(1);
                    int k = ((Cursor) localObject3).getInt(2);
                    int m = ((Cursor) localObject3).getInt(3);
                    int n = ((Cursor) localObject3).getInt(4);
                    int i = ((Cursor) localObject3).getInt(5);
                    int j = ((Cursor) localObject3).getInt(6);
                    idTurno = i2;
                    Object localObject4 = ((Cursor) localObject3).getString(8);
                    horaAlarma = 0;
                    minutoAlarma = 0;
                    if ((localObject4 != null) && (!((String) localObject4).equals("")) && (!((String) localObject4).isEmpty())) {
                        horaAlarma = Integer.parseInt(((String) localObject4).substring(0, 2));
                        minutoAlarma = Integer.parseInt(((String) localObject4).substring(3, 5));
                    }
                    localObject4 = new GregorianCalendar();
                    ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                    long l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                    i2 = ((Cursor) localObject3).getInt(7);
                    int i3 = ((Cursor) localObject3).getInt(10);
                    requestCode = numeroCalendarioBucle + 300;
                    if ((i2 > 0) && (i3 == 0) && (l1 < l2)) {
                        cancelaAlarma = 0;
                        label431:
                        modificaAlarmas();
                        localObject4 = ((Cursor) localObject3).getString(13);
                        horaAlarma = 0;
                        minutoAlarma = 0;
                        if ((localObject4 != "") && (localObject4 != null) && (!((String) localObject4).isEmpty())) {
                            horaAlarma = Integer.parseInt(((String) localObject4).substring(0, 2));
                            minutoAlarma = Integer.parseInt(((String) localObject4).substring(3, 5));
                        }
                        localObject4 = new GregorianCalendar();
                        ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                        l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                        i2 = ((Cursor) localObject3).getInt(11);
                        i3 = ((Cursor) localObject3).getInt(12);
                        requestCode = numeroCalendarioBucle + 500;
                        if ((i2 <= 0) || (i3 != 0) || (l1 >= l2)) {
                            break label4761;
                        }
                        cancelaAlarma = 0;
                        label590:
                        modificaAlarmas();
                        if (i1 <= 0) {
                            break label4810;
                        }
                        Object localObject5 = localSQLiteDatabase.rawQuery("SELECT horaAlarma, alarma, alarmaDiaAntes, horaAlarma2, alarma2, alarma2DiaAntes, codigoSecuencial, _id, horaInicio1, horaFinal1, horaInicio2, horaFinal2, turnoPartido, realizarAccion FROM tablaTurnos WHERE _id = '" + i1 + "'", null);
                        String str;
                        int i4;
                        int i5;
                        int i6;
                        int i7;
                        int i8;
                        int i9;
                        int i10;
                        int i11;
                        int i12;
                        int i13;
                        int i14;
                        if (((Cursor) localObject5).moveToFirst()) {
                            str = ((Cursor) localObject5).getString(0);
                            i1 = Integer.parseInt(str.substring(0, 2));
                            i2 = Integer.parseInt(str.substring(3, 5));
                            i3 = ((Cursor) localObject5).getInt(1);
                            i4 = ((Cursor) localObject5).getInt(2);
                            str = ((Cursor) localObject5).getString(3);
                            i5 = Integer.parseInt(str.substring(0, 2));
                            i6 = Integer.parseInt(str.substring(3, 5));
                            i7 = ((Cursor) localObject5).getInt(4);
                            i8 = ((Cursor) localObject5).getInt(5);
                            idTurno = ((Cursor) localObject5).getInt(7);
                            str = ((Cursor) localObject5).getString(8);
                            i9 = Integer.parseInt(str.substring(0, 2));
                            i10 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(9);
                            i11 = Integer.parseInt(str.substring(0, 2));
                            i12 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(10);
                            i13 = Integer.parseInt(str.substring(0, 2));
                            i14 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(11);
                            int i15 = Integer.parseInt(str.substring(0, 2));
                            int i16 = Integer.parseInt(str.substring(3, 5));
                            int i17 = ((Cursor) localObject5).getInt(12);
                            realizarAccion = ((Cursor) localObject5).getString(13);
                            variableWifi1 = Integer.parseInt(realizarAccion.substring(0, 1));
                            variableModo1 = Integer.parseInt(realizarAccion.substring(1, 2));
                            variableBT1 = Integer.parseInt(realizarAccion.substring(2, 3));
                            variableWifi2 = Integer.parseInt(realizarAccion.substring(3, 4));
                            variableModo2 = Integer.parseInt(realizarAccion.substring(4, 5));
                            variableBT2 = Integer.parseInt(realizarAccion.substring(5, 6));
                            requestCode = numeroCalendarioBucle + 1100;
                            numeroAlarma = 0;
                            horaAlarma = i9;
                            minutoAlarma = i10;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2)) {
                                break label4768;
                            }
                            cancelaAlarma = 0;
                            label1082:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 1200;
                            numeroAlarma = 0;
                            horaAlarma = i11;
                            minutoAlarma = i12;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2)) {
                                break label4775;
                            }
                            cancelaAlarma = 0;
                            label1158:
                            if ((i11 < i9) || ((i11 == i9) && (i12 < i10))) {
                                cancelaAlarma = 1;
                            }
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 1300;
                            numeroAlarma = 0;
                            horaAlarma = i13;
                            minutoAlarma = i14;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || (i17 <= 0)) {
                                break label4782;
                            }
                            cancelaAlarma = 0;
                            label1264:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 1400;
                            numeroAlarma = 0;
                            horaAlarma = i15;
                            minutoAlarma = i16;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || (i17 <= 0)) {
                                break label4789;
                            }
                            cancelaAlarma = 0;
                            label1345:
                            if ((i15 < i13) || ((i15 == i13) && (i16 < i14))) {
                                cancelaAlarma = 1;
                            }
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 11100;
                            numeroAlarma = 1;
                            horaAlarma = i1;
                            minutoAlarma = i2;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i3 <= 0) || (i4 != 0) || (m != 0) || (l1 >= l2)) {
                                break label4796;
                            }
                            cancelaAlarma = 0;
                            label1451:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 12100;
                            horaAlarma = i5;
                            minutoAlarma = i6;
                            numeroAlarma = 2;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i7 <= 0) || (i8 != 0) || (n != 0) || (l1 >= l2)) {
                                break label4803;
                            }
                            cancelaAlarma = 0;
                            label1533:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject5).close();
                        label1543:
                        if (k <= 0) {
                            break label4937;
                        }
                        localObject5 = localSQLiteDatabase.rawQuery("SELECT horaAlarma, alarma, alarmaDiaAntes, horaAlarma2, alarma2, alarma2DiaAntes, codigoSecuencial, _id, horaInicio1, horaFinal1, horaInicio2, horaFinal2, turnoPartido, realizarAccion FROM tablaTurnos WHERE _id = '" + k + "'", null);
                        if (((Cursor) localObject5).moveToFirst()) {
                            str = ((Cursor) localObject5).getString(0);
                            k = Integer.parseInt(str.substring(0, 2));
                            m = Integer.parseInt(str.substring(3, 5));
                            n = ((Cursor) localObject5).getInt(1);
                            i1 = ((Cursor) localObject5).getInt(2);
                            str = ((Cursor) localObject5).getString(3);
                            i2 = Integer.parseInt(str.substring(0, 2));
                            i3 = Integer.parseInt(str.substring(3, 5));
                            i4 = ((Cursor) localObject5).getInt(4);
                            i5 = ((Cursor) localObject5).getInt(5);
                            idTurno = ((Cursor) localObject5).getInt(7);
                            str = ((Cursor) localObject5).getString(8);
                            i6 = Integer.parseInt(str.substring(0, 2));
                            i7 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(9);
                            i8 = Integer.parseInt(str.substring(0, 2));
                            i9 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(10);
                            i10 = Integer.parseInt(str.substring(0, 2));
                            i11 = Integer.parseInt(str.substring(3, 5));
                            str = ((Cursor) localObject5).getString(11);
                            i12 = Integer.parseInt(str.substring(0, 2));
                            i13 = Integer.parseInt(str.substring(3, 5));
                            i14 = ((Cursor) localObject5).getInt(12);
                            realizarAccion = ((Cursor) localObject5).getString(13);
                            variableWifi1 = Integer.parseInt(realizarAccion.substring(0, 1));
                            variableModo1 = Integer.parseInt(realizarAccion.substring(1, 2));
                            variableBT1 = Integer.parseInt(realizarAccion.substring(2, 3));
                            variableWifi2 = Integer.parseInt(realizarAccion.substring(3, 4));
                            variableModo2 = Integer.parseInt(realizarAccion.substring(4, 5));
                            variableBT2 = Integer.parseInt(realizarAccion.substring(5, 6));
                            requestCode = numeroCalendarioBucle + 2100;
                            numeroAlarma = 0;
                            horaAlarma = i6;
                            minutoAlarma = i7;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2)) {
                                break label4895;
                            }
                            cancelaAlarma = 0;
                            label2028:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 2200;
                            numeroAlarma = 0;
                            horaAlarma = i8;
                            minutoAlarma = i9;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2)) {
                                break label4902;
                            }
                            cancelaAlarma = 0;
                            label2104:
                            if ((i8 < i6) || ((i8 == i6) && (i9 < i7))) {
                                cancelaAlarma = 1;
                            }
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 2300;
                            numeroAlarma = 0;
                            horaAlarma = i10;
                            minutoAlarma = i11;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || (i14 <= 0)) {
                                break label4909;
                            }
                            cancelaAlarma = 0;
                            label2210:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 2400;
                            numeroAlarma = 0;
                            horaAlarma = i12;
                            minutoAlarma = i13;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || (i14 <= 0)) {
                                break label4916;
                            }
                            cancelaAlarma = 0;
                            label2291:
                            if ((i8 < i6) || ((i8 == i6) && (i9 < i7))) {
                                cancelaAlarma = 1;
                            }
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 21100;
                            horaAlarma = k;
                            minutoAlarma = m;
                            numeroAlarma = 1;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((n <= 0) || (i1 != 0) || (i != 0) || (l1 >= l2)) {
                                break label4923;
                            }
                            cancelaAlarma = 0;
                            label2395:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 22100;
                            horaAlarma = i2;
                            minutoAlarma = i3;
                            numeroAlarma = 2;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i4 <= 0) || (i5 != 0) || (j != 0) || (l1 >= l2)) {
                                break label4930;
                            }
                            cancelaAlarma = 0;
                            label2476:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject5).close();
                        label2486:
                        ((Cursor) localObject3).close();
                        ((GregorianCalendar) localObject2).add(5, 1);
                        i2 = año * 10000 + mes * 100 + ((GregorianCalendar) localObject2).get(5);
                        diaAntes = 1;
                        localObject3 = localSQLiteDatabase.rawQuery("SELECT fecha, turno1, turno2, alarma1, alarma2, alarma1T2, alarma2T2, notificacion, hora, sonido, notificacionDiaAntes, notificacion2, notificacion2DiaAntes, hora2, sonido2 FROM dias WHERE fecha = '" + i2 + "'", null);
                        if (!((Cursor) localObject3).moveToFirst()) {
                            break label5319;
                        }
                        i1 = ((Cursor) localObject3).getInt(1);
                        k = ((Cursor) localObject3).getInt(2);
                        m = ((Cursor) localObject3).getInt(3);
                        n = ((Cursor) localObject3).getInt(4);
                        i = ((Cursor) localObject3).getInt(5);
                        j = ((Cursor) localObject3).getInt(6);
                        idTurno = i2;
                        localObject4 = ((Cursor) localObject3).getString(8);
                        horaAlarma = 0;
                        minutoAlarma = 0;
                        if ((localObject4 != "") && (localObject4 != null) && (!((String) localObject4).isEmpty())) {
                            horaAlarma = Integer.parseInt(((String) localObject4).substring(0, 2));
                            minutoAlarma = Integer.parseInt(((String) localObject4).substring(3, 5));
                        }
                        i2 = ((Cursor) localObject3).getInt(7);
                        i3 = ((Cursor) localObject3).getInt(10);
                        requestCode = numeroCalendarioBucle + 400;
                        localObject4 = new GregorianCalendar();
                        ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                        l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                        if ((i2 <= 0) || (i3 != 1) || (l1 >= l2)) {
                            break label5211;
                        }
                        cancelaAlarma = 0;
                        label2789:
                        modificaAlarmas();
                        localObject4 = ((Cursor) localObject3).getString(13);
                        horaAlarma = 0;
                        minutoAlarma = 0;
                        if ((localObject4 != "") && (localObject4 != null) && (!((String) localObject4).isEmpty())) {
                            horaAlarma = Integer.parseInt(((String) localObject4).substring(0, 2));
                            minutoAlarma = Integer.parseInt(((String) localObject4).substring(3, 5));
                        }
                        localObject4 = new GregorianCalendar();
                        ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                        l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                        i2 = ((Cursor) localObject3).getInt(11);
                        i3 = ((Cursor) localObject3).getInt(12);
                        requestCode = numeroCalendarioBucle + 600;
                        if ((i2 <= 0) || (i3 != 1) || (l1 >= l2)) {
                            break label5218;
                        }
                        cancelaAlarma = 0;
                        label2949:
                        modificaAlarmas();
                        if (i1 <= 0) {
                            break label5239;
                        }
                        localObject5 = localSQLiteDatabase.rawQuery("SELECT horaAlarma, alarma, alarmaDiaAntes, horaAlarma2, alarma2, alarma2DiaAntes, codigoSecuencial, _id FROM tablaTurnos WHERE _id = '" + i1 + "'", null);
                        if (((Cursor) localObject5).moveToFirst()) {
                            str = ((Cursor) localObject5).getString(0);
                            i1 = Integer.parseInt(str.substring(0, 2));
                            i2 = Integer.parseInt(str.substring(3, 5));
                            i3 = ((Cursor) localObject5).getInt(1);
                            i4 = ((Cursor) localObject5).getInt(2);
                            str = ((Cursor) localObject5).getString(3);
                            i5 = Integer.parseInt(str.substring(0, 2));
                            i6 = Integer.parseInt(str.substring(3, 5));
                            i7 = ((Cursor) localObject5).getInt(4);
                            i8 = ((Cursor) localObject5).getInt(5);
                            idTurno = ((Cursor) localObject5).getInt(7);
                            requestCode = numeroCalendarioBucle + 11200;
                            horaAlarma = i1;
                            minutoAlarma = i2;
                            numeroAlarma = 1;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i3 <= 0) || (i4 != 1) || (m != 0) || (l1 >= l2)) {
                                break label5225;
                            }
                            cancelaAlarma = 0;
                            label3199:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 12200;
                            horaAlarma = i5;
                            minutoAlarma = i6;
                            numeroAlarma = 2;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i7 <= 0) || (i8 != 1) || (n != 0) || (l1 >= l2)) {
                                break label5232;
                            }
                            cancelaAlarma = 0;
                            label3282:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject5).close();
                        label3292:
                        if (k <= 0) {
                            break label5286;
                        }
                        localObject5 = localSQLiteDatabase.rawQuery("SELECT horaAlarma, alarma, alarmaDiaAntes, horaAlarma2, alarma2, alarma2DiaAntes, codigoSecuencial, _id FROM tablaTurnos WHERE _id = '" + k + "'", null);
                        if (((Cursor) localObject5).moveToFirst()) {
                            str = ((Cursor) localObject5).getString(0);
                            k = Integer.parseInt(str.substring(0, 2));
                            m = Integer.parseInt(str.substring(3, 5));
                            n = ((Cursor) localObject5).getInt(1);
                            i1 = ((Cursor) localObject5).getInt(2);
                            str = ((Cursor) localObject5).getString(3);
                            i2 = Integer.parseInt(str.substring(0, 2));
                            i3 = Integer.parseInt(str.substring(3, 5));
                            i4 = ((Cursor) localObject5).getInt(4);
                            i5 = ((Cursor) localObject5).getInt(5);
                            idTurno = ((Cursor) localObject5).getInt(7);
                            requestCode = numeroCalendarioBucle + 21200;
                            horaAlarma = k;
                            minutoAlarma = m;
                            numeroAlarma = 1;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((n <= 0) || (i1 != 1) || (i != 0) || (l1 >= l2)) {
                                break label5272;
                            }
                            cancelaAlarma = 0;
                            label3533:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 22200;
                            horaAlarma = i2;
                            minutoAlarma = i3;
                            numeroAlarma = 2;
                            ((GregorianCalendar) localObject4).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject4).getTimeInMillis();
                            if ((i4 <= 0) || (i5 != 1) || (j != 0) || (l1 >= l2)) {
                                break label5279;
                            }
                            cancelaAlarma = 0;
                            label3615:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject5).close();
                        label3625:
                        ((Cursor) localObject3).close();
                        ((GregorianCalendar) localObject2).add(5, -2);
                        j = año * 10000 + mes * 100 + ((GregorianCalendar) localObject2).get(5);
                        localObject2 = localSQLiteDatabase.rawQuery("SELECT fecha, turno1, turno2 FROM dias WHERE fecha = '" + j + "'", null);
                        if (!((Cursor) localObject2).moveToFirst()) {
                            break label5498;
                        }
                        k = ((Cursor) localObject2).getInt(1);
                        i = ((Cursor) localObject2).getInt(2);
                        idTurno = j;
                        localObject3 = new GregorianCalendar();
                        if (k <= 0) {
                            break label5418;
                        }
                        localObject4 = localSQLiteDatabase.rawQuery("SELECT _id, horaInicio1, horaFinal1, horaInicio2, horaFinal2, turnoPartido, realizarAccion FROM tablaTurnos WHERE _id = '" + k + "'", null);
                        if (((Cursor) localObject4).moveToFirst()) {
                            idTurno = ((Cursor) localObject4).getInt(0);
                            localObject5 = ((Cursor) localObject4).getString(1);
                            j = Integer.parseInt(((String) localObject5).substring(0, 2));
                            k = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(2);
                            m = Integer.parseInt(((String) localObject5).substring(0, 2));
                            n = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(3);
                            i1 = Integer.parseInt(((String) localObject5).substring(0, 2));
                            i2 = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(4);
                            i3 = Integer.parseInt(((String) localObject5).substring(0, 2));
                            i4 = Integer.parseInt(((String) localObject5).substring(3, 5));
                            i5 = ((Cursor) localObject4).getInt(5);
                            realizarAccion = ((Cursor) localObject4).getString(6);
                            variableWifi1 = Integer.parseInt(realizarAccion.substring(0, 1));
                            variableModo1 = Integer.parseInt(realizarAccion.substring(1, 2));
                            variableBT1 = Integer.parseInt(realizarAccion.substring(2, 3));
                            variableWifi2 = Integer.parseInt(realizarAccion.substring(3, 4));
                            variableModo2 = Integer.parseInt(realizarAccion.substring(4, 5));
                            variableBT2 = Integer.parseInt(realizarAccion.substring(5, 6));
                            requestCode = numeroCalendarioBucle + 1500;
                            numeroAlarma = 0;
                            horaAlarma = m;
                            minutoAlarma = n;
                            ((GregorianCalendar) localObject3).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject3).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || ((j <= m) && ((j != m) || (k <= n)))) {
                                break label5404;
                            }
                            cancelaAlarma = 0;
                            label4120:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 1600;
                            numeroAlarma = 0;
                            horaAlarma = i3;
                            minutoAlarma = i4;
                            ((GregorianCalendar) localObject3).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject3).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || ((i1 <= i3) && ((i1 != i3) || (i2 <= i4))) || (i5 <= 0)) {
                                break label5411;
                            }
                            cancelaAlarma = 0;
                            label4222:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject4).close();
                        label4232:
                        if (i <= 0) {
                            break label5465;
                        }
                        localObject4 = localSQLiteDatabase.rawQuery("SELECT _id, horaInicio1, horaFinal1, horaInicio2, horaFinal2, turnoPartido, realizarAccion FROM tablaTurnos WHERE _id = '" + i + "'", null);
                        if (((Cursor) localObject4).moveToFirst()) {
                            idTurno = ((Cursor) localObject4).getInt(0);
                            localObject5 = ((Cursor) localObject4).getString(1);
                            i = Integer.parseInt(((String) localObject5).substring(0, 2));
                            j = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(2);
                            k = Integer.parseInt(((String) localObject5).substring(0, 2));
                            m = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(3);
                            n = Integer.parseInt(((String) localObject5).substring(0, 2));
                            i1 = Integer.parseInt(((String) localObject5).substring(3, 5));
                            localObject5 = ((Cursor) localObject4).getString(4);
                            i2 = Integer.parseInt(((String) localObject5).substring(0, 2));
                            i3 = Integer.parseInt(((String) localObject5).substring(3, 5));
                            i4 = ((Cursor) localObject4).getInt(5);
                            realizarAccion = ((Cursor) localObject4).getString(6);
                            variableWifi1 = Integer.parseInt(realizarAccion.substring(0, 1));
                            variableModo1 = Integer.parseInt(realizarAccion.substring(1, 2));
                            variableBT1 = Integer.parseInt(realizarAccion.substring(2, 3));
                            variableWifi2 = Integer.parseInt(realizarAccion.substring(3, 4));
                            variableModo2 = Integer.parseInt(realizarAccion.substring(4, 5));
                            variableBT2 = Integer.parseInt(realizarAccion.substring(5, 6));
                            requestCode = numeroCalendarioBucle + 2500;
                            numeroAlarma = 0;
                            horaAlarma = k;
                            minutoAlarma = m;
                            ((GregorianCalendar) localObject3).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject3).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || ((i <= k) && ((i != k) || (j <= m)))) {
                                break label5451;
                            }
                            cancelaAlarma = 0;
                            label4614:
                            modificaAlarmas();
                            requestCode = numeroCalendarioBucle + 2600;
                            numeroAlarma = 0;
                            horaAlarma = i2;
                            minutoAlarma = i3;
                            ((GregorianCalendar) localObject3).set(año, mes, dia, horaAlarma, minutoAlarma, 0);
                            l2 = ((GregorianCalendar) localObject3).getTimeInMillis();
                            if ((Integer.parseInt(realizarAccion) <= 0) || (l1 >= l2) || ((n <= i2) && ((n != i2) || (i1 <= i3))) || (i4 <= 0)) {
                                break label5458;
                            }
                            cancelaAlarma = 0;
                            label4716:
                            modificaAlarmas();
                        }
                        ((Cursor) localObject4).close();
                    }
                }
                for (; ; ) {
                    ((Cursor) localObject2).close();
                    localSQLiteDatabase.close();
                    ((BaseDeDatos) localObject1).close();
                    numeroCalendarioBucle += 1;
                    break;
                    cancelaAlarma = 1;
                    break label431;
                    label4761:
                    cancelaAlarma = 1;
                    break label590;
                    label4768:
                    cancelaAlarma = 1;
                    break label1082;
                    label4775:
                    cancelaAlarma = 1;
                    break label1158;
                    label4782:
                    cancelaAlarma = 1;
                    break label1264;
                    label4789:
                    cancelaAlarma = 1;
                    break label1345;
                    label4796:
                    cancelaAlarma = 1;
                    break label1451;
                    label4803:
                    cancelaAlarma = 1;
                    break label1533;
                    label4810:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 11100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 12100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1300;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1400;
                    modificaAlarmas();
                    break label1543;
                    label4895:
                    cancelaAlarma = 1;
                    break label2028;
                    label4902:
                    cancelaAlarma = 1;
                    break label2104;
                    label4909:
                    cancelaAlarma = 1;
                    break label2210;
                    label4916:
                    cancelaAlarma = 1;
                    break label2291;
                    label4923:
                    cancelaAlarma = 1;
                    break label2395;
                    label4930:
                    cancelaAlarma = 1;
                    break label2476;
                    label4937:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 21100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 22100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2300;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2400;
                    modificaAlarmas();
                    break label2486;
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 11100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 12100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 21100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 22100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 300;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 500;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1300;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1400;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2100;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2300;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2400;
                    modificaAlarmas();
                    break label2486;
                    label5211:
                    cancelaAlarma = 1;
                    break label2789;
                    label5218:
                    cancelaAlarma = 1;
                    break label2949;
                    label5225:
                    cancelaAlarma = 1;
                    break label3199;
                    label5232:
                    cancelaAlarma = 1;
                    break label3282;
                    label5239:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 11200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 12200;
                    modificaAlarmas();
                    break label3292;
                    label5272:
                    cancelaAlarma = 1;
                    break label3533;
                    label5279:
                    cancelaAlarma = 1;
                    break label3615;
                    label5286:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 21200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 22200;
                    modificaAlarmas();
                    break label3625;
                    label5319:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 11200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 12200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 21200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 22200;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 400;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 600;
                    modificaAlarmas();
                    break label3625;
                    label5404:
                    cancelaAlarma = 1;
                    break label4120;
                    label5411:
                    cancelaAlarma = 1;
                    break label4222;
                    label5418:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 1500;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1600;
                    modificaAlarmas();
                    break label4232;
                    label5451:
                    cancelaAlarma = 1;
                    break label4614;
                    label5458:
                    cancelaAlarma = 1;
                    break label4716;
                    label5465:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 2500;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2600;
                    modificaAlarmas();
                    continue;
                    label5498:
                    cancelaAlarma = 1;
                    requestCode = numeroCalendarioBucle + 1500;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 1600;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2500;
                    modificaAlarmas();
                    requestCode = numeroCalendarioBucle + 2600;
                    modificaAlarmas();
                }
            }
            leyendoAlarmas = false;
            return;
        }
        numeroCalendarioBucle = 1;
    }

    private static void modificaAlarmas() {
        if (requestCode != 0) {
            numeroCalendario = requestCode % 100;
            requestCode2 = requestCode / 100;
        }
        GregorianCalendar localGregorianCalendar = new GregorianCalendar();
        localGregorianCalendar.set(año, mes, dia, horaAlarma, minutoAlarma, 0);
        int i;
        Intent localIntent;
        if (requestCode2 > 100) {
            i = requestCode2 / 10;
            diaAntes = requestCode2 - i * 10 - 1;
            if ((requestCode2 >= 10) && (requestCode2 <= 30)) {
                break label377;
            }
            localIntent = new Intent(contextoServicio, RecibeAlarma.class);
            localIntent.setFlags(8388608);
            label118:
            Bundle localBundle = new Bundle();
            localBundle.putInt("idTurno", idTurno);
            localBundle.putInt("numeroAlarma", numeroAlarma);
            localBundle.putInt("diaAntes", diaAntes);
            localBundle.putInt("requestCode", requestCode);
            if ((requestCode2 > 10) && (requestCode2 < 30)) {
                localBundle.putInt("variableWifi1", variableWifi1);
                localBundle.putInt("variableModo1", variableModo1);
                localBundle.putInt("variableBT1", variableBT1);
                localBundle.putInt("variableWifi2", variableWifi2);
                localBundle.putInt("variableModo2", variableModo2);
                localBundle.putInt("variableBT2", variableBT2);
            }
            localIntent.putExtras(localBundle);
            am = (AlarmManager) contextoServicio.getSystemService("alarm");
            if ((requestCode2 >= 10) && (requestCode2 <= 30)) {
                break label393;
            }
            alarma = PendingIntent.getActivity(contextoServicio, requestCode, localIntent, 134217728);
            label295:
            if (cancelaAlarma != 0) {
                break label460;
            }
            i = Build.VERSION.SDK_INT;
            if (i >= 19) {
                break label412;
            }
            am.set(0, localGregorianCalendar.getTimeInMillis(), alarma);
        }
        label377:
        label393:
        label412:
        do {
            return;
            if ((requestCode2 == 3) || (requestCode2 == 5)) {
                diaAntes = 0;
                numeroAlarma = 0;
                break;
            }
            if ((requestCode2 != 4) && (requestCode2 != 6)) {
                break;
            }
            diaAntes = 1;
            numeroAlarma = 0;
            break;
            localIntent = new Intent(contextoServicio, Servicio.class);
            break label118;
            alarma = PendingIntent.getService(contextoServicio, requestCode, localIntent, 134217728);
            break label295;
            if ((19 <= i) && (i < 23)) {
                am.setExact(0, localGregorianCalendar.getTimeInMillis(), alarma);
                return;
            }
        } while (i < 23);
        am.setExactAndAllowWhileIdle(0, localGregorianCalendar.getTimeInMillis(), alarma);
        return;
        label460:
        am.cancel(alarma);
    }

    public static void paraAlarmas() {
        mp.reset();
        if (pref.getBoolean("vibracionAlarmas", true)) {
            vibrador.cancel();
        }
    }

    */
/* Error *//*

    public static void reproduceAlarma() {
        // Byte code:
        //   0: getstatic 63	com/lrhsoft/shiftercalendar/Servicio:sonido	Ljava/lang/String;
        //   3: ifnull +209 -> 212
        //   6: getstatic 63	com/lrhsoft/shiftercalendar/Servicio:sonido	Ljava/lang/String;
        //   9: ldc 89
        //   11: invokevirtual 208	java/lang/String:equals	(Ljava/lang/Object;)Z
        //   14: ifne +198 -> 212
        //   17: getstatic 63	com/lrhsoft/shiftercalendar/Servicio:sonido	Ljava/lang/String;
        //   20: invokevirtual 211	java/lang/String:isEmpty	()Z
        //   23: ifne +189 -> 212
        //   26: new 365	java/io/File
        //   29: dup
        //   30: getstatic 63	com/lrhsoft/shiftercalendar/Servicio:sonido	Ljava/lang/String;
        //   33: invokespecial 367	java/io/File:<init>	(Ljava/lang/String;)V
        //   36: invokevirtual 370	java/io/File:exists	()Z
        //   39: ifeq +98 -> 137
        //   42: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   45: getstatic 63	com/lrhsoft/shiftercalendar/Servicio:sonido	Ljava/lang/String;
        //   48: invokevirtual 373	android/media/MediaPlayer:setDataSource	(Ljava/lang/String;)V
        //   51: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   54: invokevirtual 376	android/media/MediaPlayer:prepare	()V
        //   57: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   60: invokevirtual 379	android/media/MediaPlayer:start	()V
        //   63: getstatic 340	com/lrhsoft/shiftercalendar/Servicio:pref	Landroid/content/SharedPreferences;
        //   66: ldc_w 342
        //   69: iconst_1
        //   70: invokeinterface 348 3 0
        //   75: ifeq +13 -> 88
        //   78: getstatic 350	com/lrhsoft/shiftercalendar/Servicio:vibrador	Landroid/os/Vibrator;
        //   81: getstatic 73	com/lrhsoft/shiftercalendar/Servicio:patronVibracion	[J
        //   84: iconst_0
        //   85: invokevirtual 383	android/os/Vibrator:vibrate	([JI)V
        //   88: return
        //   89: astore_0
        //   90: aload_0
        //   91: invokevirtual 386	java/lang/IllegalArgumentException:printStackTrace	()V
        //   94: goto -43 -> 51
        //   97: astore_0
        //   98: aload_0
        //   99: invokevirtual 387	java/lang/SecurityException:printStackTrace	()V
        //   102: goto -51 -> 51
        //   105: astore_0
        //   106: aload_0
        //   107: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   110: goto -59 -> 51
        //   113: astore_0
        //   114: aload_0
        //   115: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   118: goto -67 -> 51
        //   121: astore_0
        //   122: aload_0
        //   123: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   126: goto -69 -> 57
        //   129: astore_0
        //   130: aload_0
        //   131: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   134: goto -77 -> 57
        //   137: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   140: getstatic 161	com/lrhsoft/shiftercalendar/Servicio:contextoServicio	Landroid/content/Context;
        //   143: getstatic 391	com/lrhsoft/shiftercalendar/Servicio:sonidoDefecto	Landroid/net/Uri;
        //   146: invokevirtual 394	android/media/MediaPlayer:setDataSource	(Landroid/content/Context;Landroid/net/Uri;)V
        //   149: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   152: invokevirtual 376	android/media/MediaPlayer:prepare	()V
        //   155: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   158: invokevirtual 379	android/media/MediaPlayer:start	()V
        //   161: goto -98 -> 63
        //   164: astore_0
        //   165: aload_0
        //   166: invokevirtual 386	java/lang/IllegalArgumentException:printStackTrace	()V
        //   169: goto -20 -> 149
        //   172: astore_0
        //   173: aload_0
        //   174: invokevirtual 387	java/lang/SecurityException:printStackTrace	()V
        //   177: goto -28 -> 149
        //   180: astore_0
        //   181: aload_0
        //   182: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   185: goto -36 -> 149
        //   188: astore_0
        //   189: aload_0
        //   190: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   193: goto -44 -> 149
        //   196: astore_0
        //   197: aload_0
        //   198: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   201: goto -46 -> 155
        //   204: astore_0
        //   205: aload_0
        //   206: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   209: goto -54 -> 155
        //   212: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   215: getstatic 161	com/lrhsoft/shiftercalendar/Servicio:contextoServicio	Landroid/content/Context;
        //   218: getstatic 391	com/lrhsoft/shiftercalendar/Servicio:sonidoDefecto	Landroid/net/Uri;
        //   221: invokevirtual 394	android/media/MediaPlayer:setDataSource	(Landroid/content/Context;Landroid/net/Uri;)V
        //   224: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   227: invokevirtual 376	android/media/MediaPlayer:prepare	()V
        //   230: getstatic 333	com/lrhsoft/shiftercalendar/Servicio:mp	Landroid/media/MediaPlayer;
        //   233: invokevirtual 379	android/media/MediaPlayer:start	()V
        //   236: goto -173 -> 63
        //   239: astore_0
        //   240: aload_0
        //   241: invokevirtual 386	java/lang/IllegalArgumentException:printStackTrace	()V
        //   244: goto -20 -> 224
        //   247: astore_0
        //   248: aload_0
        //   249: invokevirtual 387	java/lang/SecurityException:printStackTrace	()V
        //   252: goto -28 -> 224
        //   255: astore_0
        //   256: aload_0
        //   257: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   260: goto -36 -> 224
        //   263: astore_0
        //   264: aload_0
        //   265: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   268: goto -44 -> 224
        //   271: astore_0
        //   272: aload_0
        //   273: invokevirtual 388	java/lang/IllegalStateException:printStackTrace	()V
        //   276: goto -46 -> 230
        //   279: astore_0
        //   280: aload_0
        //   281: invokevirtual 389	java/io/IOException:printStackTrace	()V
        //   284: goto -54 -> 230
        // Local variable table:
        //   start	length	slot	name	signature
        //   89	2	0	localIllegalArgumentException1	IllegalArgumentException
        //   97	2	0	localSecurityException1	SecurityException
        //   105	2	0	localIllegalStateException1	IllegalStateException
        //   113	2	0	localIOException1	java.io.IOException
        //   121	2	0	localIllegalStateException2	IllegalStateException
        //   129	2	0	localIOException2	java.io.IOException
        //   164	2	0	localIllegalArgumentException2	IllegalArgumentException
        //   172	2	0	localSecurityException2	SecurityException
        //   180	2	0	localIllegalStateException3	IllegalStateException
        //   188	2	0	localIOException3	java.io.IOException
        //   196	2	0	localIllegalStateException4	IllegalStateException
        //   204	2	0	localIOException4	java.io.IOException
        //   239	2	0	localIllegalArgumentException3	IllegalArgumentException
        //   247	2	0	localSecurityException3	SecurityException
        //   255	2	0	localIllegalStateException5	IllegalStateException
        //   263	2	0	localIOException5	java.io.IOException
        //   271	2	0	localIllegalStateException6	IllegalStateException
        //   279	2	0	localIOException6	java.io.IOException
        // Exception table:
        //   from	to	target	type
        //   42	51	89	java/lang/IllegalArgumentException
        //   42	51	97	java/lang/SecurityException
        //   42	51	105	java/lang/IllegalStateException
        //   42	51	113	java/io/IOException
        //   51	57	121	java/lang/IllegalStateException
        //   51	57	129	java/io/IOException
        //   137	149	164	java/lang/IllegalArgumentException
        //   137	149	172	java/lang/SecurityException
        //   137	149	180	java/lang/IllegalStateException
        //   137	149	188	java/io/IOException
        //   149	155	196	java/lang/IllegalStateException
        //   149	155	204	java/io/IOException
        //   212	224	239	java/lang/IllegalArgumentException
        //   212	224	247	java/lang/SecurityException
        //   212	224	255	java/lang/IllegalStateException
        //   212	224	263	java/io/IOException
        //   224	230	271	java/lang/IllegalStateException
        //   224	230	279	java/io/IOException
    }

    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    public void onCreate() {
        registerReceiver(this.m_timeChangedReceiver, s_intentFilter);
        contextoServicio = this;
        mp = new MediaPlayer();
        sonidoDefecto = Uri.parse("android.resource://com.lrhsoft.shiftercalendar/raw/alarma");
        mp.setAudioStreamType(4);
        audio = (AudioManager) getSystemService("audio");
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.m_timeChangedReceiver);
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        super.onStartCommand(paramIntent, paramInt1, paramInt2);
        audio = (AudioManager) getSystemService("audio");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        vibrador = (Vibrator) getSystemService("vibrator");
        float f = Integer.parseInt(pref.getString("volumenAlarma", "5")) / 5.0F;
        mp.setVolume(f, f);
        int m;
        int n;
        int i1;
        int i3;
        int i4;
        int i5;
        int i;
        label178:
        int j;
        label187:
        int k;
        if (paramIntent != null) {
            paramIntent = paramIntent.getExtras();
            if (paramIntent != null) {
                paramInt2 = paramIntent.getInt("requestCode");
                m = paramIntent.getInt("variableWifi1");
                n = paramIntent.getInt("variableModo1");
                i1 = paramIntent.getInt("variableBT1");
                i3 = paramIntent.getInt("variableWifi2");
                i4 = paramIntent.getInt("variableModo2");
                i5 = paramIntent.getInt("variableBT2");
                paramInt1 = paramInt2;
                if (paramInt2 != 0) {
                    paramInt1 = paramInt2 / 100;
                }
                if (paramInt1 != 11) {
                    break label377;
                }
                paramInt2 = 1;
                if (paramInt1 != 13) {
                    break label382;
                }
                i = 1;
                if (paramInt1 != 21) {
                    break label388;
                }
                j = 1;
                if (paramInt1 != 23) {
                    break label394;
                }
                k = 1;
                label196:
                if ((k | paramInt2 | i | j) == 0) {
                    break label472;
                }
                if (m != 1) {
                    break label400;
                }
                ((WifiManager) getSystemService("wifi")).setWifiEnabled(true);
                label230:
                audio = (AudioManager) getBaseContext().getSystemService("audio");
                if (n != 1) {
                    break label424;
                }
                audio.setRingerMode(2);
                label259:
                if (i1 != 1) {
                    break label456;
                }
                BluetoothAdapter.getDefaultAdapter().enable();
            }
        }
        label272:
        paramIntent = new GregorianCalendar();
        paramIntent.set(paramIntent.get(1), paramIntent.get(2), paramIntent.get(5), 0, 0, 0);
        paramIntent.add(5, 1);
        Object localObject = new Intent(contextoServicio, Servicio.class);
        localObject = PendingIntent.getService(contextoServicio, 0, (Intent) localObject, 134217728);
        AlarmManager localAlarmManager = (AlarmManager) contextoServicio.getSystemService("alarm");
        paramInt1 = Build.VERSION.SDK_INT;
        if (paramInt1 < 19) {
            localAlarmManager.set(0, paramIntent.getTimeInMillis(), (PendingIntent) localObject);
        }
        for (; ; ) {
            leeAlarmas();
            return 1;
            label377:
            paramInt2 = 0;
            break;
            label382:
            i = 0;
            break label178;
            label388:
            j = 0;
            break label187;
            label394:
            k = 0;
            break label196;
            label400:
            if (m != 2) {
                break label230;
            }
            ((WifiManager) getSystemService("wifi")).setWifiEnabled(false);
            break label230;
            label424:
            if (n == 2) {
                audio.setRingerMode(1);
                break label259;
            }
            if (n != 3) {
                break label259;
            }
            audio.setRingerMode(0);
            break label259;
            label456:
            if (i1 != 2) {
                break label272;
            }
            BluetoothAdapter.getDefaultAdapter().disable();
            break label272;
            label472:
            label480:
            label489:
            label498:
            label507:
            label516:
            label525:
            label534:
            int i2;
            if (paramInt1 == 12) {
                paramInt2 = 1;
                if (paramInt1 != 14) {
                    break label639;
                }
                i = 1;
                if (paramInt1 != 15) {
                    break label645;
                }
                j = 1;
                if (paramInt1 != 16) {
                    break label651;
                }
                k = 1;
                if (paramInt1 != 22) {
                    break label657;
                }
                m = 1;
                if (paramInt1 != 24) {
                    break label663;
                }
                n = 1;
                if (paramInt1 != 25) {
                    break label669;
                }
                i1 = 1;
                if (paramInt1 != 26) {
                    break label675;
                }
                i2 = 1;
                label543:
                if ((i2 | paramInt2 | i | j | k | m | n | i1) == 0) {
                    break label753;
                }
                if (i3 != 1) {
                    break label681;
                }
                ((WifiManager) getSystemService("wifi")).setWifiEnabled(true);
                label589:
                audio = (AudioManager) getBaseContext().getSystemService("audio");
                if (i4 != 1) {
                    break label705;
                }
                audio.setRingerMode(2);
            }
            for (; ; ) {
                if (i5 != 1) {
                    break label737;
                }
                BluetoothAdapter.getDefaultAdapter().enable();
                break;
                paramInt2 = 0;
                break label480;
                label639:
                i = 0;
                break label489;
                label645:
                j = 0;
                break label498;
                label651:
                k = 0;
                break label507;
                label657:
                m = 0;
                break label516;
                label663:
                n = 0;
                break label525;
                label669:
                i1 = 0;
                break label534;
                label675:
                i2 = 0;
                break label543;
                label681:
                if (i3 != 2) {
                    break label589;
                }
                ((WifiManager) getSystemService("wifi")).setWifiEnabled(false);
                break label589;
                label705:
                if (i4 == 2) {
                    audio.setRingerMode(1);
                } else if (i4 == 3) {
                    audio.setRingerMode(0);
                }
            }
            label737:
            if (i5 != 2) {
                break label272;
            }
            BluetoothAdapter.getDefaultAdapter().disable();
            break label272;
            label753:
            if (paramInt1 != 0) {
                break label272;
            }
            paramIntent = new Intent(this, WidgetCalendario.class);
            paramIntent.setAction("com.lrhsoft.shiftercalendar.ACTUALIZAR_WIDGET_EXTERIOR");
            paramIntent.putExtra("appWidgetIds", AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetCalendario.class)));
            sendBroadcast(paramIntent);
            paramIntent = new Intent(this, WidgetSemana.class);
            paramIntent.setAction("com.lrhsoft.shiftercalendar.ACTUALIZAR_WIDGET");
            paramIntent.putExtra("appWidgetIds", AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetSemana.class)));
            sendBroadcast(paramIntent);
            break label272;
            if ((19 <= paramInt1) && (paramInt1 < 23)) {
                localAlarmManager.setExact(0, paramIntent.getTimeInMillis(), (PendingIntent) localObject);
            } else if (paramInt1 >= 23) {
                localAlarmManager.setExactAndAllowWhileIdle(0, paramIntent.getTimeInMillis(), (PendingIntent) localObject);
            }
        }
    }
}


*/
/* Location:              /Users/Antonio/Desktop/dex2jar-2.0/apk-dex2jar.jar!/com/lrhsoft/shiftercalendar/Servicio.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 *//*

}
*/
