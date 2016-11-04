package com.example.antonio.gestiontrabajotemporal.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Calendarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Operarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Puestos;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Turnos;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Fichajes;

/**
 * Clase que administra la conexión de la base de datos y su estructuración
 */
public class BaseDatosFichajes extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "Fichajes.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String CALENDARIO = "CALENDARIO";
        String PUESTO = "PUESTO";
        String OPERARIO = "OPERARIO";
        String TURNO = "TURNO";
        String FICHAJE = "FICHAJE";
    }

    interface Referencias {

        String ID_CALENDARIO = String.format("REFERENCES %s(%s)",
                Tablas.CALENDARIO, Calendarios.ID);

        String ID_PUESTO = String.format("REFERENCES %s(%s)",
                Tablas.PUESTO, Puestos.ID);

        String ID_OPERARIO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                Tablas.OPERARIO, Operarios.ID);

        String ID_TURNO = String.format("REFERENCES %s(%s)",
                Tablas.TURNO, Turnos.ID);

    }

    public BaseDatosFichajes(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            /*Para hacer efectiva las referencias de integridad de las llaves foráneas en SQLite
            es necesario usar el método setForeignKeyConstraintsEnabled() pasando el valor true
            dentro de onOpen(). Este método va desde Android Jelly Bean en adelante.*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                /*Para el soporte, se puede habilitar manualmente a través de la sentencia
                PRAGMA foreign_keys=ON.*/
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Crear tabla Calendarios.
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE)",
                Tablas.CALENDARIO, Calendarios.ID, Calendarios.NOMBRE));
        // Crear tabla Puestos.
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL UNIQUE)",
                Tablas.PUESTO, Puestos.ID, Puestos.NOMBRE));
        //Crear tabla Operarios.
        db.execSQL(String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY," +
                        "%s TEXT NOT NULL UNIQUE, %s TEXT NOT NULL, %s TEXT NOT NULL," +
                        "%s TEXT, %s TEXT NOT NULL, %s DATETIME NOT NULL, %s TEXT NOT NULL," +
                        "%s TEXT NOT NULL, %s DATETIME, %s TEXT NOT NULL, %s TEXT NOT NULL)",
                Tablas.OPERARIO, Operarios.ID, Operarios.DNI, Operarios.NOMBRE,
                Operarios.APELLIDOS, Operarios.FOTO, Operarios.DIRECCION, Operarios.FECHA_NACIMIENTO,
                Operarios.TELEFONO, Operarios.EMAIL, Operarios.FECHA_INICIO, Operarios.NUMERO_S_S,
                Operarios.PASSWORD));
        //Crear tabla Turnos.
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, %s INTEGER NOT NULL, %s TEXT, %s TEXT, %s REAL NOT NULL," +
                        "%s REAL NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL, %s REAL NOT NULL," +
                        "%s INTEGER NOT NULL, %s TEXT, %s INTEGER NOT NULL, %s TEXT, %s TEXT)",
                Tablas.TURNO, Turnos.ID, Turnos.NOMBRE, Turnos.ABREVIATURA_NOMBRE_TURNO,
                Turnos.HORA_INICIO_1, Turnos.HORA_FIN_1, Turnos.TURNO_PARTIDO, Turnos.HORA_INICIO_2,
                Turnos.HORA_FIN_2, Turnos.HORAS_TRABAJADAS, Turnos.HORAS_TRABAJADAS_NOCTURNAS,
                Turnos.PRECIO_HORA, Turnos.PRECIO_HORA_NOCTURNAS, Turnos.PRECIO_HORA_EXTRA,
                Turnos.AVISO, Turnos.SONIDO_ALARMA, Turnos.AVISO_DIA_ANTES, Turnos.HORA_AVISO,
                Turnos.MODO_TELEFONO));
        // Crear tabla Fichajes.
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY %s," +
                        "%s TEXT UNIQUE NOT NULL, %s INTEGER NOT NULL %s , %s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL %s , %s REAL)",
                Tablas.FICHAJE, Fichajes.ID_OPERARIO, Referencias.ID_OPERARIO, Fichajes.FECHA,
                Fichajes.ID_TURNO, Referencias.ID_TURNO, Fichajes.ID_PUESTO, Referencias.ID_PUESTO,
                Fichajes.ID_CALENDARIO, Referencias.ID_CALENDARIO, Fichajes.HORA_EXTRA));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CALENDARIO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.PUESTO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.OPERARIO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TURNO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.FICHAJE);

        onCreate(db);
    }


}