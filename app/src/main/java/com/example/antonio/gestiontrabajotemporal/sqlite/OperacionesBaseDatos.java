package com.example.antonio.gestiontrabajotemporal.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;


import com.example.antonio.gestiontrabajotemporal.modelo.Calendario;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.modelo.Puesto;
import com.example.antonio.gestiontrabajotemporal.modelo.Turno;
import com.example.antonio.gestiontrabajotemporal.sqlite.BaseDatosFichajes.Tablas;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Calendarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Operarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Puestos;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Turnos;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Fichajes;

/**
 * Clase auxiliar que implementa a {@link BaseDatosFichajes} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */
public final class OperacionesBaseDatos {

    private static BaseDatosFichajes baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();


    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatosFichajes(contexto);
        }
        return instancia;
    }

    // [OPERACIONES_PUESTO
    public Cursor obtenerPuestos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.PUESTO);

        return db.rawQuery(sql, null);
    }

    public String insertarPuesto(Puesto puesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idPuesto = Puestos.generarIdPuesto();

        ContentValues valores = new ContentValues();
        valores.put(Puestos.ID, puesto.idPuesto);
        valores.put(Puestos.NOMBRE, puesto.nombrePuesto);

        db.insertOrThrow(Tablas.PUESTO, null, valores);

        return puesto.nombrePuesto;

    }

    public boolean actualizarPuesto(Puesto puesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Puestos.NOMBRE, puesto.nombrePuesto);

        String whereClause = String.format("%s=?", Puestos.ID);
        String[] whereArgs = {puesto.idPuesto};

        int resultado = db.update(Tablas.PUESTO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarPuesto(String idPuesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Puestos.ID);
        String[] whereArgs = {idPuesto};

        int resultado = db.delete(Tablas.PUESTO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_PUESTO]

    // [OPERACIONES_CALENDARIO]
    public Cursor obtenerCalendarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CALENDARIO);

        return db.rawQuery(sql, null);
    }

    public String insertarCalendario(Calendario calendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCalendario = Calendarios.generarIdCalendario();


        ContentValues valores = new ContentValues();
        valores.put(Calendarios.ID, calendario.idCalendario);
        valores.put(Calendarios.NOMBRE, calendario.nombreCalendario);

        db.insertOrThrow(Tablas.CALENDARIO, null, valores);

        return calendario.nombreCalendario;

    }

    public boolean actualizarCalendario(Calendario calendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Calendarios.NOMBRE, calendario.nombreCalendario);

        String whereClause = String.format("%s=?", Calendarios.ID);
        String[] whereArgs = {calendario.idCalendario};

        int resultado = db.update(Tablas.CALENDARIO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCalendario(String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Calendarios.ID);
        String[] whereArgs = {idCalendario};

        int resultado = db.delete(Tablas.CALENDARIO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CALENDARIO]

    // [OPERACIONES_OPERARIO]
    public Cursor obtenerOperarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.OPERARIO);

        return db.rawQuery(sql, null);
    }

    public String insertarOperario(Operario operario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(Operarios.ID, operario.idOperario);
        valores.put(Operarios.DNI, operario.dni);
        valores.put(Operarios.NOMBRE, operario.nombre);
        valores.put(Operarios.APELLIDOS, operario.apellidos);
        valores.put(Operarios.FOTO, operario.foto);
        valores.put(Operarios.DIRECCION, operario.direccion);
        valores.put(Operarios.FECHA_NACIMIENTO, operario.fechaNacimiento);
        valores.put(Operarios.TELEFONO, operario.telefono);
        valores.put(Operarios.EMAIL, operario.email);
        valores.put(Operarios.FECHA_INICIO, operario.fechaInicio);
        valores.put(Operarios.NUMERO_S_S, operario.numeroSS);
        valores.put(Operarios.PASSWORD, operario.password);

        db.insertOrThrow(Tablas.OPERARIO, null, valores);

        return operario.idOperario;

    }

    public boolean actualizarOperario(Operario operario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Operarios.DNI, operario.dni);
        valores.put(Operarios.NOMBRE, operario.nombre);
        valores.put(Operarios.APELLIDOS, operario.apellidos);
        valores.put(Operarios.FOTO, operario.foto);
        valores.put(Operarios.DIRECCION, operario.direccion);
        valores.put(Operarios.FECHA_NACIMIENTO, operario.fechaNacimiento);
        valores.put(Operarios.TELEFONO, operario.telefono);
        valores.put(Operarios.EMAIL, operario.email);
        valores.put(Operarios.FECHA_INICIO, operario.fechaInicio);
        valores.put(Operarios.NUMERO_S_S, operario.numeroSS);
        valores.put(Operarios.PASSWORD, operario.password);


        String whereClause = String.format("%s=?", Operarios.ID);
        String[] whereArgs = {operario.idOperario};

        int resultado = db.update(Tablas.OPERARIO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarOperario(String idOperario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Operarios.ID);
        String[] whereArgs = {idOperario};

        int resultado = db.delete(Tablas.OPERARIO, whereClause, whereArgs);

        return resultado > 0;
    }

    public String obtenerPasswordOperarioId(String idOperario) throws CursorIndexOutOfBoundsException {

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        Cursor cursor = db.query("Operario", new String[]{Operarios.PASSWORD}, Operarios.ID+"=?", new String[]{idOperario}, null, null, null);

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(Operarios.PASSWORD));
        cursor.close();
        return password;
    }
    // [/OPERACIONES_OPERARIO]

    // [OPERACIONES_TURNO]
    public Cursor obtenerTurnos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.TURNO);

        return db.rawQuery(sql, null);
    }

    public String insertarTurno(Turno turno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idTurno = Turnos.generarIdTurno();

        ContentValues valores = new ContentValues();

        valores.put(Turnos.ID, turno.idTurno);
        valores.put(Turnos.NOMBRE, turno.nombreTurno);
        valores.put(Turnos.ABREVIATURA_NOMBRE_TURNO, turno.abreviaturaNombreTurno);
        valores.put(Turnos.HORA_INICIO_1, turno.horaInicio1);
        valores.put(Turnos.HORA_FIN_1, turno.horaFin1);
        valores.put(Turnos.TURNO_PARTIDO, turno.turnoPartido);
        valores.put(Turnos.HORA_INICIO_2, turno.horaInicio2);
        valores.put(Turnos.HORA_FIN_2, turno.horaFin2);
        valores.put(Turnos.HORAS_TRABAJADAS, turno.horasTrabajadas);
        valores.put(Turnos.HORAS_TRABAJADAS_NOCTURNAS, turno.horasTrabajadasNocturnas);
        valores.put(Turnos.PRECIO_HORA, turno.precioHora);
        valores.put(Turnos.PRECIO_HORA_NOCTURNAS, turno.precioHoraNocturnas);
        valores.put(Turnos.PRECIO_HORA_EXTRA, turno.precioHoraExtra);
        valores.put(Turnos.AVISO, turno.aviso);
        valores.put(Turnos.SONIDO_ALARMA, turno.sonidoAlarma);
        valores.put(Turnos.AVISO_DIA_ANTES, turno.avisoDiaAntes);
        valores.put(Turnos.HORA_AVISO, turno.horaAviso);
        valores.put(Turnos.MODO_TELEFONO, turno.modoTelefono);


        db.insertOrThrow(Tablas.TURNO, null, valores);

        return turno.idTurno;

    }

    public boolean actualizarTurno(Turno turno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Turnos.NOMBRE, turno.nombreTurno);
        valores.put(Turnos.ABREVIATURA_NOMBRE_TURNO, turno.abreviaturaNombreTurno);
        valores.put(Turnos.HORA_INICIO_1, turno.horaInicio1);
        valores.put(Turnos.HORA_FIN_1, turno.horaFin1);
        valores.put(Turnos.TURNO_PARTIDO, turno.turnoPartido);
        valores.put(Turnos.HORA_INICIO_2, turno.horaInicio2);
        valores.put(Turnos.HORAS_TRABAJADAS, turno.horasTrabajadas);
        valores.put(Turnos.HORAS_TRABAJADAS_NOCTURNAS, turno.horasTrabajadasNocturnas);
        valores.put(Turnos.PRECIO_HORA, turno.precioHora);
        valores.put(Turnos.PRECIO_HORA_NOCTURNAS, turno.precioHoraNocturnas);
        valores.put(Turnos.PRECIO_HORA_EXTRA, turno.precioHoraExtra);
        valores.put(Turnos.AVISO, turno.aviso);
        valores.put(Turnos.SONIDO_ALARMA, turno.sonidoAlarma);
        valores.put(Turnos.AVISO_DIA_ANTES, turno.avisoDiaAntes);
        valores.put(Turnos.HORA_AVISO, turno.horaAviso);
        valores.put(Turnos.MODO_TELEFONO, turno.modoTelefono);

        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {turno.idTurno};

        int resultado = db.update(Tablas.TURNO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarTurno(String idTurno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {idTurno};

        int resultado = db.delete(Tablas.TURNO, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_TURNO]

    // [OPERACIONES_FICHAJE
    public Cursor obtenerFichajes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO);

        return builder.query(db, proyFichaje, null, null, null, null, null);
    }

   /* public Cursor obtenerCabeceraPorId(String id) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=?", CabecerasPedido.ID);
        String[] selectionArgs = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO);

        String[] proyeccion = {
                Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID,
                CabecerasPedido.FECHA,
                Clientes.NOMBRES,
                Clientes.APELLIDOS,
                FormasPago.NOMBRE};

        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }*/

    public String insertarFichaje(Fichaje fichaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Fichajes.ID_OPERARIO, fichaje.idOperario);
        valores.put(Fichajes.FECHA, fichaje.fecha);
        valores.put(Fichajes.ID_TURNO, fichaje.idTurno);
        valores.put(Fichajes.ID_PUESTO, fichaje.idPuesto);
        valores.put(Fichajes.ID_CALENDARIO, fichaje.idCalendario);
        valores.put(Fichajes.HORA_EXTRA, fichaje.horaExtra);

        // Insertar fichaje
        db.insertOrThrow(Tablas.FICHAJE, null, valores);

        return fichaje.idOperario;
    }

    public boolean actualizarFichaje(Fichaje fichaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Fichajes.ID_TURNO, fichaje.idTurno);
        valores.put(Fichajes.ID_PUESTO, fichaje.idPuesto);
        valores.put(Fichajes.ID_CALENDARIO, fichaje.idCalendario);
        valores.put(Fichajes.HORA_EXTRA, fichaje.horaExtra);

        String whereClause = String.format("%s=?, %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA);
        String[] whereArgs = {fichaje.idOperario, fichaje.fecha};

        int resultado = db.update(Tablas.FICHAJE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarFichaje(String idOperario, String fecha) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?, %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA);
        String[] whereArgs = {idOperario, fecha};

        int resultado = db.delete(Tablas.FICHAJE, whereClause, whereArgs);

        return resultado > 0;
    }
    // [/OPERACIONES_CABECERA_PEDIDO]


    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    public void close() {
        baseDatos.close();
    }

    private static final String FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO = "FICHAJE " +
            "INNER JOIN OPERARIO " +
            "ON FICHAJE.idOperario= OPERARIO.idOperario " +
            "INNER JOIN TURNO " +
            "ON FICHAJE.idTurno = TURNO.idTurno" +
            "INNER JOIN PUESTO " +
            "ON FICHAJE.idPuesto = PUESTO.idPuesto" +
            "INNER JOIN CALENDARIO " +
            "ON FICHAJE.idCalendario = CALENDARIO.idCalendario";


    private final String[] proyFichaje = new String[]{
            Tablas.FICHAJE + "." + Fichajes.ID_OPERARIO,
            Fichajes.FECHA,
            Fichajes.ID_TURNO,
            Fichajes.ID_PUESTO,
            Fichajes.ID_CALENDARIO,
            Fichajes.HORA_EXTRA};

}
