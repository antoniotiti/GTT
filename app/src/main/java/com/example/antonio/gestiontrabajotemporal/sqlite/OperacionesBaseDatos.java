package com.example.antonio.gestiontrabajotemporal.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.antonio.gestiontrabajotemporal.modelo.Calendario;
import com.example.antonio.gestiontrabajotemporal.modelo.Fichaje;
import com.example.antonio.gestiontrabajotemporal.modelo.Operario;
import com.example.antonio.gestiontrabajotemporal.modelo.Puesto;
import com.example.antonio.gestiontrabajotemporal.modelo.Turno;
import com.example.antonio.gestiontrabajotemporal.sqlite.BaseDatosFichajes.Tablas;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Calendarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Fichajes;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Operarios;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Puestos;
import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos.Turnos;

/**
 * Clase auxiliar que implementa a {@link BaseDatosFichajes} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 */
public final class OperacionesBaseDatos {

    private static final String FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO = "FICHAJE " +
            "INNER JOIN OPERARIO " +
            "ON FICHAJE.idTurno= OPERARIO.idTurno " +
            "INNER JOIN TURNO " +
            "ON FICHAJE.idTurno = TURNO.idTurno" +
            "INNER JOIN PUESTO " +
            "ON FICHAJE.idTurno = PUESTO.idTurno" +
            "INNER JOIN CALENDARIO " +
            "ON FICHAJE.idTurno = CALENDARIO.idTurno";

    private static BaseDatosFichajes baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();

    private final String[] proyFichaje = new String[]{
            Tablas.FICHAJE + "." + Fichajes.ID_OPERARIO,
            Fichajes.FECHA,
            Fichajes.ID_TURNO,
            Fichajes.ID_PUESTO,
            Fichajes.ID_CALENDARIO,
            Fichajes.HORA_EXTRA};

    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new BaseDatosFichajes(contexto);
        }
        return instancia;
    }

    // [OPERACIONES_PUESTO]
    public Cursor obtenerPuestos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.PUESTO);

        return db.rawQuery(sql, null);
    }

    public Cursor obtenerPuestoById(String puestoId) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor c = db.query(
                Tablas.PUESTO,
                null,
                Puestos.ID + " LIKE ?",
                new String[]{puestoId},
                null,
                null,
                null);
        return c;
    }

    public String insertarPuesto(Puesto puesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idPuestoInsertado = null;
        // Generar Pk
        String idPuesto = Puestos.generarIdPuesto();

        ContentValues valores = new ContentValues();
        valores.put(Puestos.ID, idPuesto);
        valores.put(Puestos.NOMBRE, puesto.nombrePuesto);
        valores.put(Puestos.DESCRIPCION, puesto.descripcionPuesto);

        try {
            db.insertOrThrow(Tablas.PUESTO, null, valores);
            idPuestoInsertado = idPuesto;
        } catch (SQLiteConstraintException e) {

        }
        return idPuestoInsertado;
    }

    public int editarPuesto(Puesto puesto, String idPuesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        int puestosEditados = 0;

        ContentValues valores = new ContentValues();
        valores.put(Puestos.NOMBRE, puesto.nombrePuesto);
        valores.put(Puestos.DESCRIPCION, puesto.descripcionPuesto);

        String whereClause = String.format("%s=?", Puestos.ID);
        String[] whereArgs = {idPuesto};
        try {
            puestosEditados = db.update(Tablas.PUESTO, valores, whereClause, whereArgs);

        } catch (SQLiteConstraintException e) {

        }

        return puestosEditados;
    }

    public int eliminarPuesto(String idPuesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Puestos.ID);
        String[] whereArgs = {idPuesto};

        return db.delete(Tablas.PUESTO, whereClause, whereArgs);
    }
    // [OPERACIONES_PUESTO]

    // [OPERACIONES_CALENDARIO]
    public Cursor obtenerCalendarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CALENDARIO);

        return db.rawQuery(sql, null);
    }

    public Cursor obtenerCalendarioById(String calendarioId) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor c = db.query(
                Tablas.CALENDARIO,
                null,
                Calendarios.ID + " LIKE ?",
                new String[]{calendarioId},
                null,
                null,
                null);
        return c;
    }

    public Cursor obtenerIdCalendarioByNombre(String nombreCalendario) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor c = db.query(
                Tablas.CALENDARIO,
                new String[]{Calendarios.ID},
                Calendarios.NOMBRE + " LIKE ?",
                new String[]{nombreCalendario},
                null,
                null,
                null);
        return c;
    }

    public String insertarCalendario(Calendario calendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idCalendarioInsertado = null;

        // Generar Pk
        String idCalendario = Calendarios.generarIdCalendario();

        ContentValues valores = new ContentValues();
        valores.put(Calendarios.ID, idCalendario);
        valores.put(Calendarios.NOMBRE, calendario.nombreCalendario);
        valores.put(Calendarios.DESCRIPCION, calendario.descripcionCalendario);

        try {
            db.insertOrThrow(Tablas.CALENDARIO, null, valores);
            idCalendarioInsertado = idCalendario;
        } catch (SQLiteConstraintException e) {

        }

        return idCalendarioInsertado;
    }

    public int editarCalendario(Calendario calendario, String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        int calendariosEditados = 0;

        ContentValues valores = new ContentValues();
        valores.put(Calendarios.NOMBRE, calendario.nombreCalendario);
        valores.put(Calendarios.DESCRIPCION, calendario.descripcionCalendario);

        String whereClause = String.format("%s=?", Calendarios.ID);
        String[] whereArgs = {idCalendario};

        try {
            calendariosEditados = db.update(Tablas.CALENDARIO, valores, whereClause, whereArgs);

        } catch (SQLiteConstraintException e) {

        }

        return calendariosEditados;
    }

    public int eliminarCalendario(String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Calendarios.ID);
        String[] whereArgs = {idCalendario};


        return db.delete(Tablas.CALENDARIO, whereClause, whereArgs);
    }
    // [OPERACIONES_CALENDARIO]

    // [OPERACIONES_OPERARIO]
    public Cursor obtenerOperarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.OPERARIO);

        return db.rawQuery(sql, null);
    }

    public Cursor obtenerOperarioById(String operarioId) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor c = db.query(
                Tablas.OPERARIO,
                null,
                Operarios.ID + " LIKE ?",
                new String[]{operarioId},
                null,
                null,
                null);
        return c;
    }

    public String insertarOperario(Operario operario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idOperarioInsertado = null;

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

        try {
            db.insertOrThrow(Tablas.OPERARIO, null, valores);
            idOperarioInsertado = operario.idOperario;
        } catch (SQLiteConstraintException e) {

        }

        return idOperarioInsertado;

    }

    public boolean editarOperario(Operario operario, String idOperario) {
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
        String[] whereArgs = {idOperario};

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

        Cursor cursor = db.query(
                Tablas.OPERARIO,
                new String[]{Operarios.PASSWORD},
                Operarios.ID + "=?",
                new String[]{idOperario},
                null,
                null,
                null);

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(Operarios.PASSWORD));
        cursor.close();
        return password;
    }
    // [OPERACIONES_OPERARIO]

    // [OPERACIONES_TURNO]
    public Cursor obtenerTurnos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.TURNO);

        return db.rawQuery(sql, null);
    }

    public Cursor obtenerTurnoById(String turnoId) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        Cursor c = db.query(
                Tablas.TURNO,
                null,
                Turnos.ID + " LIKE ?",
                new String[]{turnoId},
                null,
                null,
                null);
        return c;
    }

    /*public boolean eliminarTurno(String idTurno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {idTurno};

        int resultado = db.delete(Tablas.TURNO, whereClause, whereArgs);

        return resultado > 0;
    }

    public int eliminarTurno(String idTurno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        return db.delete(
                Tablas.TURNO,
                Turnos.ID + " LIKE ?",
                new String[]{idTurno});
    }*/

    public String insertarTurno(Turno turno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idTurnoInsertado = null;

        // Generar Pk
        String idTurno = Turnos.generarIdTurno();

        ContentValues valores = new ContentValues();

        valores.put(Turnos.ID, idTurno);
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
        valores.put(Turnos.AVISO_DIA_ANTES, turno.avisoDiaAntes);
        valores.put(Turnos.HORA_AVISO, turno.horaAviso);
        valores.put(Turnos.MODO_TELEFONO, turno.modoTelefono);
        valores.put(Turnos.COLOR_FONDO, turno.colorFondo);
        valores.put(Turnos.COLOR_TEXTO, turno.colorTexto);
        try {
            db.insertOrThrow(Tablas.TURNO, null, valores);
            idTurnoInsertado = idTurno;
        } catch (SQLiteConstraintException e) {

        }
        return idTurnoInsertado;

    }

    public int editarTurno(Turno turno, String turnoId) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        int turnosEditados = 0;

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
        valores.put(Turnos.AVISO_DIA_ANTES, turno.avisoDiaAntes);
        valores.put(Turnos.HORA_AVISO, turno.horaAviso);
        valores.put(Turnos.MODO_TELEFONO, turno.modoTelefono);
        valores.put(Turnos.COLOR_FONDO, turno.colorFondo);
        valores.put(Turnos.COLOR_TEXTO, turno.colorTexto);

        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {turnoId};

        try {
            turnosEditados = db.update(Tablas.TURNO, valores, whereClause, whereArgs);

        } catch (SQLiteConstraintException e) {

        }

        return turnosEditados;
    }

    public int eliminarTurno(String idTurno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {idTurno};

        return db.delete(Tablas.TURNO, whereClause, whereArgs);
    }
    // [OPERACIONES_TURNO]

    // [OPERACIONES_FICHAJE]

    public long insertarFichaje(Fichaje fichaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        long fichajeInsertado = 0;

        ContentValues valores = new ContentValues();
        valores.put(Fichajes.ID_OPERARIO, fichaje.idOperario);
        valores.put(Fichajes.FECHA, fichaje.fecha);
        valores.put(Fichajes.ID_TURNO, fichaje.idTurno);
        valores.put(Fichajes.ID_PUESTO, fichaje.idPuesto);
        valores.put(Fichajes.ID_CALENDARIO, fichaje.idCalendario);
        valores.put(Fichajes.HORA_EXTRA, fichaje.horaExtra);

        try {
            // Insertar fichaje
            fichajeInsertado = db.insertOrThrow(Tablas.FICHAJE, null, valores);
        } catch (SQLiteConstraintException e) {

        }
        return fichajeInsertado;
    }

    public boolean editarFichaje(Fichaje fichaje) {
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

        String whereClause = String.format("%s=? AND %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA);
        String[] whereArgs = {idOperario, fecha};

        int resultado = db.delete(Tablas.FICHAJE, whereClause, whereArgs);

        return resultado > 0;
    }

    public Cursor obtenerFichajeFecha(String idOperario, String fecha) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=? AND %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA);
        String[] whereArgs = {idOperario, fecha};

        Cursor resultado = db.query(Tablas.FICHAJE, null, whereClause, whereArgs, null, null, null);

        return resultado;
    }

    public Cursor obtenerFichajes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO);

        return builder.query(db, proyFichaje, null, null, null, null, null);
    }

    public Cursor obtenerFichajes(String idOperario, String calendario) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT " + Operarios.ID + ", " + Fichajes.ID_TURNO + ", " + Fichajes.ID_PUESTO
                + ", " + Fichajes.FECHA + ", " + Fichajes.HORA_EXTRA + ", " + Turnos.NOMBRE + ", "
                + Turnos.ABREVIATURA_NOMBRE_TURNO + ", " + Turnos.COLOR_FONDO + ", " + Turnos.COLOR_TEXTO
                + ", " + Calendarios.ID
                + " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ")"
                + " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO + " LIKE '" + calendario + "'";

        Cursor c = db.rawQuery(sql, null);

       /* Cursor c = db.query(
                Tablas.FICHAJE,
                null,
                Fichajes.ID_OPERARIO + " LIKE ? AND " + Fichajes.ID_CALENDARIO + " LIKE ?",
                new String[]{idOperario, calendario},
                null,
                null,
                null);*/
        return c;
    }

    public Cursor obtenerFichajes(String idOperario, String calendario,String fecha) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT " + Fichajes.HORA_EXTRA + ", " + Turnos.ABREVIATURA_NOMBRE_TURNO + ", "
                + Turnos.COLOR_FONDO + ", " + Turnos.COLOR_TEXTO +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " LIKE '" + fecha + "'";

        Cursor c = db.rawQuery(sql, null);

       /* Cursor c = db.query(
                Tablas.FICHAJE,
                null,
                Fichajes.ID_OPERARIO + " LIKE ? AND " + Fichajes.ID_CALENDARIO + " LIKE ?",
                new String[]{idOperario, calendario},
                null,
                null,
                null);*/
        return c;
    }


    /*SELECT O.idOperario, F.id_turno, F.id_puesto, F.fecha, F.horaExtra,  T.nombreTurno, T.abreviaturaNombreTurno, T.colorFondo, T.colorTexto, C.idCalendario
FROM fichaje F INNER JOIN turno T ON (F.id_turno = T.idTurno) INNER JOIN operario O ON (F.id_operario = O.idOperario) INNER JOIN calendario C ON (F.id_calendario = C.idCalendario)
WHERE O.idOperario LIKE '8246' AND C.idCalendario like 'CA-a9854abd-eb2c-4f10-b124-245ab996d711'*/


    // [OPERACIONES_FICHAJE]

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    public void close() {
        baseDatos.close();
    }

}
