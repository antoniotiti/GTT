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

    //[INICIO OPERACIONES_PUESTO]

    /**
     * Método que se encarga de obtener todos los puestos de la BBDD.
     *
     * @return Cursor con todos los Puestos obtenidos
     */
    public Cursor obtenerPuestos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.PUESTO);
        return db.rawQuery(sql, null);
    }

    /**
     * Método que se encarga de obtener un puesto dado su id por parámetro.
     *
     * @param puestoId Id del puesto que queremos obtener los datos.
     * @return Cursor con el puesto obtenido.
     */
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

    /**
     * Método que se encarga de obtener el id del puesto dado su nombre por parámetro.
     *
     * @param nombrePuesto Nombre del puesto que queremos obtener el id.
     * @return Id del puesto obtenido.
     */
    public Cursor obtenerIdPuestoByNombre(String nombrePuesto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        Cursor c = db.query(
                Tablas.PUESTO,
                new String[]{Puestos.ID},
                Puestos.NOMBRE + " LIKE ?",
                new String[]{nombrePuesto},
                null,
                null,
                null);
        return c;
    }

    /**
     * Método que se encarga de insertar un puesto en la BBDD.
     *
     * @param puesto Puesto a insertar en la BBDD.
     * @return Id del puesto insertado.
     */
    public String insertarPuesto(Puesto puesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idPuestoInsertado = null;
        String idPuesto = Puestos.generarIdPuesto(); //Generamos un id aleatorio

        ContentValues valores = new ContentValues();
        valores.put(Puestos.ID, idPuesto);
        valores.put(Puestos.NOMBRE, puesto.nombrePuesto);
        valores.put(Puestos.DESCRIPCION, puesto.descripcionPuesto);

        try {
            db.insertOrThrow(Tablas.PUESTO, null, valores);
            idPuestoInsertado = idPuesto;
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return idPuestoInsertado;
    }

    /**
     * Método que se encarga de editar un puesto.
     *
     * @param puesto   Datos del puesto que queremos editar
     * @param idPuesto ID del puesto que queremos editar
     * @return Devuelve 1 si se ha editado correctamente el puesto, 0 si no se ha editado.
     */
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
            e.printStackTrace();
        }
        return puestosEditados;
    }

    /**
     * Método que se encarga de borrar un puesto dado su id por parámetro.
     *
     * @param idPuesto Id del puesto a borrar
     * @return Devuelve 1 si se ha borrado correctamente el puesto. 0 si no se ha borrado.
     */
    public int eliminarPuesto(String idPuesto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", Puestos.ID);
        String[] whereArgs = {idPuesto};
        return db.delete(Tablas.PUESTO, whereClause, whereArgs);
    }
    //[FIN OPERACIONES_PUESTO]

    //[INICIO OPERACIONES_CALENDARIO]

    /**
     * Método que se encarga de obtener todos los calendarios de la BBDD.
     *
     * @return Cursor con todos los calendarios obtenidos.
     */
    public Cursor obtenerCalendarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.CALENDARIO);
        return db.rawQuery(sql, null);
    }

    /**
     * Método que se encarga de obtener un calendario dado su id por parámetro.
     *
     * @param calendarioId Id del calendario del que queremos obteer los datos
     * @return Cursor con los datos del calendario
     */
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

    /**
     * Método que se encarga de obtener el id del calendario dado su nombre por parámetro.
     *
     * @param nombreCalendario Nombre del calendario
     * @return Id del calendario obtenido
     */
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

    /**
     * Método que se encarga de insertar un calendario en la BBDD.
     *
     * @param calendario Calendario a insertar en la BBDD.
     * @return Id del calendario insertado.
     */
    public String insertarCalendario(Calendario calendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idCalendarioInsertado = null;
        String idCalendario = Calendarios.generarIdCalendario(); //Generamos un id aleatorio

        ContentValues valores = new ContentValues();
        valores.put(Calendarios.ID, idCalendario);
        valores.put(Calendarios.NOMBRE, calendario.nombreCalendario);
        valores.put(Calendarios.DESCRIPCION, calendario.descripcionCalendario);

        try {
            db.insertOrThrow(Tablas.CALENDARIO, null, valores);
            idCalendarioInsertado = idCalendario;
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return idCalendarioInsertado;
    }

    /**
     * Método que se encarga de editar un calendario.
     *
     * @param calendario   Datos del calendario que queremos editar
     * @param idCalendario ID del calendario que queremos editar
     * @return Devuelve 1 si se ha editado correctamente el calendario, 0 si no se ha editado.
     */
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
            e.printStackTrace();
        }
        return calendariosEditados;
    }

    /**
     * Método que se encarga de borrar un calendario dado su id por parámetro.
     *
     * @param idCalendario Id del calendario a borrar
     * @return Devuelve 1 si se ha borrado correctamente el calendario. 0 si no se ha borrado.
     */
    public int eliminarCalendario(String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", Calendarios.ID);
        String[] whereArgs = {idCalendario};
        return db.delete(Tablas.CALENDARIO, whereClause, whereArgs);
    }
    // [FIN OPERACIONES_CALENDARIO]

    // [INICIO OPERACIONES_OPERARIO]

    /**
     * Método que se encarga de obtener todos los operarios de la BBDD.
     *
     * @return Cursor con todos los operarios obtenidos.
     */
    public Cursor obtenerOperarios() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.OPERARIO);
        return db.rawQuery(sql, null);
    }

    /**
     * Método que se encarga de obtener un operarios dado su id por parámetro.
     *
     * @param operarioId Id del operarios del que queremos obteer los datos
     * @return Cursor con los datos del operarios
     */
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

    /**
     * Método que se encarga de insertar un operario en la BBDD.
     *
     * @param operario Operario a insertar en la BBDD.
     * @return Id del operario insertado.
     */
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
            e.printStackTrace();
        }
        return idOperarioInsertado;
    }

    /**
     * Método que se encarga de editar un operario.
     *
     * @param operario   Datos del operario que queremos editar
     * @param idOperario ID del operario que queremos editar
     * @return Devuelve true si se ha editado correctamente el operario, false si no se ha editado.
     */
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

    /**
     * Método que se encarga de borrar un operario dado su id por parámetro.
     *
     * @param idOperario Id del operario a borrar
     * @return Devuelve true si se ha borrado correctamente el operario. False si no se ha borrado.
     */
    public boolean eliminarOperario(String idOperario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", Operarios.ID);
        String[] whereArgs = {idOperario};

        int resultado = db.delete(Tablas.OPERARIO, whereClause, whereArgs);
        return resultado > 0;
    }

    /**
     * Método encargado de obtener el password de un usuario mediante el id del operario dado por parámetro.
     *
     * @param idOperario Identificador del operario
     * @return El password del operario.
     * @throws CursorIndexOutOfBoundsException
     */
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
        String password = cursor.getString(cursor.getColumnIndex(Operarios.PASSWORD)); //Obtenemos el password
        cursor.close();//Cerramos el cursor
        return password;
    }

    /**
     * Método encargado de obtener el password de un usuario mediante el id y email del operario dado por parámetro.
     *
     * @param idOperario Identificador del operario
     * @param email      Email del operario
     * @return El password del operario.
     * @throws CursorIndexOutOfBoundsException
     */
    public String obtenerPasswordOperarioId(String idOperario, String email) throws CursorIndexOutOfBoundsException {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=? AND %s=?", Operarios.ID, Operarios.EMAIL);
        String[] whereArgs = {idOperario, email};

        Cursor cursor = db.query(Tablas.OPERARIO,
                new String[]{Operarios.PASSWORD},
                whereClause,
                whereArgs,
                null,
                null,
                null);

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(Operarios.PASSWORD)); //Obtenemos el password
        cursor.close(); //Cerramos el cursor
        return password;
    }
    // [FIN OPERACIONES_OPERARIO]

    // [OPERACIONES_TURNO]

    /**
     * Método que se encarga de obtener todos los turnos de la BBDD.
     *
     * @return Cursor con todos los turnos obtenidos.
     */
    public Cursor obtenerTurnos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.TURNO);
        return db.rawQuery(sql, null);
    }

    /**
     * Método que se encarga de obtener un turno dado su id por parámetro.
     *
     * @param turnoId Id del turno del que queremos obtener los datos
     * @return Cursor con los datos del turno
     */
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

    /**
     * Método que se encarga de insertar un turno en la BBDD.
     *
     * @param turno Turno a insertar en la BBDD.
     * @return Id del turno insertado.
     */
    public String insertarTurno(Turno turno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String idTurnoInsertado = null;
        String idTurno = Turnos.generarIdTurno();//Generamos un id aleatorio

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
            e.printStackTrace();
        }
        return idTurnoInsertado;
    }

    /**
     * Método que se encarga de editar un turno.
     *
     * @param turno   Datos del turno que queremos editar
     * @param turnoId ID del turno que queremos editar
     * @return Devuelve 1 si se ha editado correctamente el turno, 0 si no se ha editado.
     */
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
            e.printStackTrace();
        }
        return turnosEditados;
    }

    /**
     * Método que se encarga de borrar un turno dado su id por parámetro.
     *
     * @param idTurno Id del turno a borrar
     * @return Devuelve 1 si se ha borrado correctamente el turno. 0 si no se ha borrado.
     */
    public int eliminarTurno(String idTurno) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", Turnos.ID);
        String[] whereArgs = {idTurno};
        return db.delete(Tablas.TURNO, whereClause, whereArgs);
    }
    // [FIN OPERACIONES_TURNO]

    // [INICIO OPERACIONES_FICHAJE]

    /**
     * Método que se encarga de insertar un fichaje en la BBDD.
     *
     * @param fichaje Fichaje a insertar en la BBDD.
     * @return Id del fichaje insertado.
     */
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
        valores.put(Fichajes.COMENTARIO, fichaje.comentario);
        try {
            // Insertar fichaje
            fichajeInsertado = db.insertOrThrow(Tablas.FICHAJE, null, valores);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }
        return fichajeInsertado;
    }

    /**
     * Método que se encarga de editar un fichaje.
     *
     * @param fichaje Datos del fichaje que queremos editar
     * @return Devuelve true si se ha editado correctamente el fichaje, false si no se ha editado.
     */
    public boolean editarFichaje(Fichaje fichaje) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Fichajes.ID_TURNO, fichaje.idTurno);
        valores.put(Fichajes.ID_PUESTO, fichaje.idPuesto);
        valores.put(Fichajes.ID_CALENDARIO, fichaje.idCalendario);
        valores.put(Fichajes.HORA_EXTRA, fichaje.horaExtra);
        valores.put(Fichajes.COMENTARIO, fichaje.comentario);

        String whereClause = String.format("%s=? AND %s=? AND %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA, Fichajes.ID_CALENDARIO);
        String[] whereArgs = {fichaje.idOperario, fichaje.fecha, fichaje.idCalendario};

        int resultado = db.update(Tablas.FICHAJE, valores, whereClause, whereArgs);
        return resultado > 0;
    }

    /**
     * Método que se encarga de borrar un Fichaje dado el id del operario, la fecha del fichaje
     * y el id del calendario por parámetro.
     *
     * @param idOperario   Id del operario
     * @param fecha        Fecha
     * @param idCalendario Id del calendario
     * @return Devuelve true si se ha borrado correctamente el fichaje. False si no se ha borrado.
     */
    public boolean eliminarFichaje(String idOperario, String fecha, String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=? AND %s=? AND %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA, Fichajes.ID_CALENDARIO);
        String[] whereArgs = {idOperario, fecha, idCalendario};

        int resultado = db.delete(Tablas.FICHAJE, whereClause, whereArgs);
        return resultado > 0;
    }

    /**
     * Método que se encarga de obtener un fichaje dado el id del operario, la fecha y el id del
     * calendario por parámetro.
     *
     * @param idOperario   Id del operario
     * @param fecha        Fecha
     * @param idCalendario Id del calendario
     * @return Cursor con los datos del fichaje
     */
    public Cursor obtenerFichajeFecha(String idOperario, String fecha, String idCalendario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=? AND %s=? AND %s=?", Fichajes.ID_OPERARIO, Fichajes.FECHA, Fichajes.ID_CALENDARIO);
        String[] whereArgs = {idOperario, fecha, idCalendario};

        Cursor resultado = db.query(Tablas.FICHAJE, null, whereClause, whereArgs, null, null, null);
        return resultado;
    }

    public Cursor obtenerFichajesParaCalendario() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FICHAJE_JOIN_OPERARIO_TURNO_PUESTO_Y_CALENDARIO);
        return builder.query(db, proyFichaje, null, null, null, null, null);
    }

    /**
     * Método que se encarga de obtener los datos necesarios del fichaje para mostrarlos
     * en la pantalla de calendario.
     *
     * @param idOperario Id del operario
     * @param calendario Id del calendario
     * @return Cursor con los datos del fichaje
     */
    public Cursor obtenerFichajesParaCalendario(String idOperario, String calendario) {
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
        return c;
    }

    /**
     * Método que se encarga de obtener los datos necesarios del fichaje para mostrarlos
     * en la pantalla de calendario.
     *
     * @param idOperario Id del operario
     * @param calendario Id del calendario
     * @param fecha      Fecha
     * @return Cursor con los datos del fichaje
     */
    public Cursor obtenerFichajesParaCalendario(String idOperario, String calendario, String fecha) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = "SELECT " + Fichajes.HORA_EXTRA + ", " + Turnos.ABREVIATURA_NOMBRE_TURNO + ", "
                + Turnos.COLOR_FONDO + ", " + Turnos.COLOR_TEXTO +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " LIKE '" + fecha + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * Método que se encarga de obtener los datos necesarios del fichaje para mostrarlos
     * en la pantalla de detalle.
     *
     * @param idOperario Id del operario
     * @param calendario Id del calendario
     * @param fecha      Fecha
     * @return Cursor con los datos del fichaje
     */
    public Cursor obtenerFichajesParaDetalle(String idOperario, String calendario, String fecha) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = "SELECT " + Fichajes.HORA_EXTRA + ", " + Fichajes.COMENTARIO + ", "
                + Turnos.ABREVIATURA_NOMBRE_TURNO + ", " + Turnos.NOMBRE + ", " + Turnos.ID + ", "
                + Turnos.COLOR_FONDO + ", " + Turnos.COLOR_TEXTO + ", " + Operarios.NOMBRE + ", "
                + Operarios.APELLIDOS + ", " + Puestos.NOMBRE + ", " + Puestos.ID +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ") INNER JOIN " + Tablas.PUESTO + " ON (" + Fichajes.ID_PUESTO
                + " = " + Puestos.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO
                + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " LIKE '" + fecha + "'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * Método que se encarga de obtener los datos de la alarma
     *
     * @param idOperario Id del operario
     * @param calendario Id del calendario
     * @param fecha      Fecha
     * @return Cursor con los datos de la alarma
     */
    public Cursor obtenerAlarmaFichaje(String idOperario, String calendario, String fecha) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = "SELECT " + Turnos.AVISO + ", " + Turnos.HORA_AVISO + ", " + Turnos.NOMBRE + ", "
                + Turnos.HORA_INICIO_1 + ", " + Fichajes.COMENTARIO + ", " + Puestos.NOMBRE +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ") INNER JOIN " + Tablas.PUESTO + " ON (" + Fichajes.ID_PUESTO
                + " = " + Puestos.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO
                + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " LIKE '" + fecha + "'";

        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * Método que se encarga de obtener los datos del modo del teléfono
     *
     * @param idOperario Id del operario
     * @param calendario Id del calendario
     * @param fecha      Fecha
     * @return Cursor con los datos del modo del teléfono
     */
    public Cursor obtenerModoTelefonoFichaje(String idOperario, String calendario, String fecha) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = "SELECT " + Turnos.MODO_TELEFONO +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO
                + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " LIKE '" + fecha + "'";

        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * Método encargado de obtener los datos necesarios para calcular la nómina.
     *
     * @param calendario   Id del calendario
     * @param idOperario   Id del operario
     * @param primerDiaMes Fecha del primer día del mes que se ha trabajado
     * @param ultimoDiaMes Fecha del último día del mes que se ha trabajado
     * @return Datos necesarios para calcular la nómina
     */
    public Cursor obtenerDatosNomina(String calendario, String idOperario, String primerDiaMes, String ultimoDiaMes) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = "SELECT " + Fichajes.HORA_EXTRA + ", " + Turnos.HORAS_TRABAJADAS + ", "
                + Turnos.HORAS_TRABAJADAS_NOCTURNAS + ", " + Turnos.PRECIO_HORA + ", "
                + Turnos.PRECIO_HORA_EXTRA + ", " + Turnos.PRECIO_HORA_NOCTURNAS +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ") INNER JOIN " + Tablas.PUESTO + " ON (" + Fichajes.ID_PUESTO
                + " = " + Puestos.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO
                + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " >= Date ('" + primerDiaMes + "') AND "
                + Fichajes.FECHA + " <= Date ('" + ultimoDiaMes + "')";

        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /**
     * Método encargado de obtener los datos necesarios para calcular la nómina en detalle.
     *
     * @param calendario   Id del calendario
     * @param idOperario   Id del operario
     * @param primerDiaMes Fecha del primer día del mes que se ha trabajado
     * @param ultimoDiaMes Fecha del último día del mes que se ha trabajado
     * @return Datos necesarios para calcular la nómina en detalle
     */
    public Cursor obtenerDatosNominaDetalle(String calendario, String idOperario, String primerDiaMes, String ultimoDiaMes) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = "SELECT " + Fichajes.HORA_EXTRA + ", " + Turnos.HORAS_TRABAJADAS + ", "
                + Turnos.HORAS_TRABAJADAS_NOCTURNAS + ", " + Turnos.PRECIO_HORA + ", "
                + Turnos.PRECIO_HORA_EXTRA + ", " + Turnos.PRECIO_HORA_NOCTURNAS + ", " + Turnos.NOMBRE +
                ", " + Puestos.NOMBRE + ", " + Operarios.FECHA_INICIO + ", " + Operarios.NUMERO_S_S +
                ", " + Operarios.NOMBRE + ", " + Operarios.APELLIDOS + ", " + Operarios.DIRECCION +
                ", " + Operarios.DNI + ", " + Operarios.EMAIL + ", " + Operarios.TELEFONO +
                ", " + Operarios.FOTO + ", " + Operarios.FECHA_NACIMIENTO +
                " FROM " + Tablas.FICHAJE + " INNER JOIN " + Tablas.TURNO + " ON (" + Fichajes.ID_TURNO
                + " = " + Turnos.ID + ") INNER JOIN " + Tablas.OPERARIO + " ON (" + Fichajes.ID_OPERARIO
                + " = " + Operarios.ID + ") INNER JOIN " + Tablas.CALENDARIO + " ON (" + Fichajes.ID_CALENDARIO
                + " = " + Calendarios.ID + ") INNER JOIN " + Tablas.PUESTO + " ON (" + Fichajes.ID_PUESTO
                + " = " + Puestos.ID + ")" +
                " WHERE " + Fichajes.ID_OPERARIO + " LIKE '" + idOperario + "' AND " + Fichajes.ID_CALENDARIO
                + " LIKE '" + calendario + "' AND " + Fichajes.FECHA + " >= Date ('" + primerDiaMes + "') AND "
                + Fichajes.FECHA + " <= Date ('" + ultimoDiaMes + "')";

        Cursor c = db.rawQuery(sql, null);
        return c;

    }
    // [FIN OPERACIONES_FICHAJE]

    /**
     * Metodo que se encarga de abrir la BBDD en modo escritura.
     *
     * @return
     */
    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    /**
     * Método que se encarga de cerrar la BBDD.
     */
    public void close() {
        baseDatos.close();
    }
}