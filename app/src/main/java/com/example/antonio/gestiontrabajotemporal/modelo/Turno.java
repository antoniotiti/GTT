package com.example.antonio.gestiontrabajotemporal.modelo;


import android.database.Cursor;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;

public class Turno {

    public String idTurno, nombreTurno, abreviaturaNombreTurno, horaInicio1, horaFin1, horaInicio2, horaFin2, horaAviso, modoTelefono;
    public int turnoPartido, aviso, avisoDiaAntes, colorFondo, colorTexto;
    public float horasTrabajadas, horasTrabajadasNocturnas;
    public double precioHora, precioHoraNocturnas, precioHoraExtra;

    public Turno(String idTurno, String nombreTurno, String abreviaturaNombreTurno, String horaInicio1, String horaFin1, int turnoPartido, String horaInicio2, String horaFin2, float horasTrabajadas, float horasTrabajadasNocturnas, double precioHora, double precioHoraNocturnas, double precioHoraExtra, int aviso, int avisoDiaAntes, String horaAviso, String modoTelefono, int colorFondo, int colorTexto) {
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
        this.abreviaturaNombreTurno = abreviaturaNombreTurno;
        this.horaInicio1 = horaInicio1;
        this.horaFin1 = horaFin1;
        this.turnoPartido = turnoPartido;
        this.horaInicio2 = horaInicio2;
        this.horaFin2 = horaFin2;
        this.horasTrabajadas = horasTrabajadas;
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
        this.precioHora = precioHora;
        this.precioHoraNocturnas = precioHoraNocturnas;
        this.precioHoraExtra = precioHoraExtra;
        this.aviso = aviso;
        this.avisoDiaAntes = avisoDiaAntes;
        this.horaAviso = horaAviso;
        this.modoTelefono = modoTelefono;
        this.colorFondo = colorFondo;
        this.colorTexto = colorTexto;
    }

    public Turno(String nombreTurno, String abreviaturaNombreTurno, String horaInicio1, String horaFin1, int turnoPartido, String horaInicio2, String horaFin2, float horasTrabajadas, float horasTrabajadasNocturnas, double precioHora, double precioHoraNocturnas, double precioHoraExtra, int aviso, int avisoDiaAntes, String horaAviso, String modoTelefono, int colorFondo, int colorTexto) {
        this.idTurno = null;
        this.nombreTurno = nombreTurno;
        this.abreviaturaNombreTurno = abreviaturaNombreTurno;
        this.horaInicio1 = horaInicio1;
        this.horaFin1 = horaFin1;
        this.turnoPartido = turnoPartido;
        this.horaInicio2 = horaInicio2;
        this.horaFin2 = horaFin2;
        this.horasTrabajadas = horasTrabajadas;
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
        this.precioHora = precioHora;
        this.precioHoraNocturnas = precioHoraNocturnas;
        this.precioHoraExtra = precioHoraExtra;
        this.aviso = aviso;
        this.avisoDiaAntes = avisoDiaAntes;
        this.horaAviso = horaAviso;
        this.modoTelefono = modoTelefono;
        this.colorFondo = colorFondo;
        this.colorTexto = colorTexto;
    }

    public Turno(Cursor cursor) {
        this.idTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.ID));
        this.nombreTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.NOMBRE));
        this.abreviaturaNombreTurno = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.ABREVIATURA_NOMBRE_TURNO));
        this.horaInicio1 = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_INICIO_1));
        this.horaFin1 = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_FIN_1));
        this.turnoPartido = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.TURNO_PARTIDO));
        this.horaInicio2 = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_INICIO_2));
        this.horaFin2 = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_FIN_2));
        this.horasTrabajadas = cursor.getFloat(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS));
        this.horasTrabajadasNocturnas = cursor.getFloat(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORAS_TRABAJADAS_NOCTURNAS));
        this.precioHora = cursor.getDouble(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA));
        this.precioHoraNocturnas = cursor.getDouble(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_NOCTURNAS));
        this.precioHoraExtra = cursor.getDouble(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.PRECIO_HORA_EXTRA));
        this.aviso = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.AVISO));
        this.avisoDiaAntes = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.AVISO_DIA_ANTES));
        this.horaAviso = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.HORA_AVISO));
        this.modoTelefono = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.MODO_TELEFONO));
        this.colorFondo = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_FONDO));
        this.colorTexto = cursor.getInt(cursor.getColumnIndex(NombresColumnasBaseDatos.Turnos.COLOR_TEXTO));
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public void setNombreTurno(String nombreTurno) {
        this.nombreTurno = nombreTurno;
    }

    public String getAbreviaturaNombreTurno() {
        return abreviaturaNombreTurno;
    }

    public void setAbreviaturaNombreTurno(String abreviaturaNombreTurno) {
        this.abreviaturaNombreTurno = abreviaturaNombreTurno;
    }

    public String getHoraInicio1() {
        return horaInicio1;
    }

    public void setHoraInicio1(String horaInicio1) {
        this.horaInicio1 = horaInicio1;
    }

    public String getHoraFin1() {
        return horaFin1;
    }

    public void setHoraFin1(String horaFin1) {
        this.horaFin1 = horaFin1;
    }

    public int getTurnoPartido() {
        return turnoPartido;
    }

    public void setTurnoPartido(int turnoPartido) {
        this.turnoPartido = turnoPartido;
    }

    public String getHoraInicio2() {
        return horaInicio2;
    }

    public void setHoraInicio2(String horaInicio2) {
        this.horaInicio2 = horaInicio2;
    }

    public String getHoraFin2() {
        return horaFin2;
    }

    public void setHoraFin2(String horaFin2) {
        this.horaFin2 = horaFin2;
    }

    public float getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(float horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public float getHorasTrabajadasNocturnas() {
        return horasTrabajadasNocturnas;
    }

    public void setHorasTrabajadasNocturnas(float horasTrabajadasNocturnas) {
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(float precioHora) {
        this.precioHora = precioHora;
    }

    public double getPrecioHoraNocturnas() {
        return precioHoraNocturnas;
    }

    public void setPrecioHoraNocturnas(float precioHoraNocturnas) {
        this.precioHoraNocturnas = precioHoraNocturnas;
    }

    public double getPrecioHoraExtra() {
        return precioHoraExtra;
    }

    public void setPrecioHoraExtra(float precioHoraExtra) {
        this.precioHoraExtra = precioHoraExtra;
    }

    public int getAviso() {
        return aviso;
    }

    public void setAviso(int aviso) {
        this.aviso = aviso;
    }

    public int getAvisoDiaAntes() {
        return avisoDiaAntes;
    }

    public void setAvisoDiaAntes(int avisoDiaAntes) {
        this.avisoDiaAntes = avisoDiaAntes;
    }

    public String getHoraAviso() {
        return horaAviso;
    }

    public void setHoraAviso(String horaAviso) {
        this.horaAviso = horaAviso;
    }

    public String getModoTelefono() {
        return modoTelefono;
    }

    public void setModoTelefono(String modoTelefono) {
        this.modoTelefono = modoTelefono;
    }

    public int getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(int colorFondo) {
        this.colorFondo = colorFondo;
    }

    public int getColorTexto() {
        return colorTexto;
    }

    public void setColorTexto(int colorTexto) {
        this.colorTexto = colorTexto;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "idTurno='" + idTurno + '\'' +
                ", nombreTurno='" + nombreTurno + '\'' +
                ", abreviaturaNombreTurno='" + abreviaturaNombreTurno + '\'' +
                ", horaInicio1='" + horaInicio1 + '\'' +
                ", horaFin1='" + horaFin1 + '\'' +
                ", turnoPartido=" + turnoPartido +
                ", horaInicio2='" + horaInicio2 + '\'' +
                ", horaFin2='" + horaFin2 + '\'' +
                ", horasTrabajadas=" + horasTrabajadas +
                ", horasTrabajadasNocturnas=" + horasTrabajadasNocturnas +
                ", precioHora=" + precioHora +
                ", precioHoraNocturnas=" + precioHoraNocturnas +
                ", precioHoraExtra=" + precioHoraExtra +
                ", aviso=" + aviso +
                ", avisoDiaAntes=" + avisoDiaAntes +
                ", horaAviso='" + horaAviso + '\'' +
                ", modoTelefono='" + modoTelefono + '\'' +
                ", colorFondo='" + colorFondo + '\'' +
                ", colorTexto='" + colorTexto + '\'' +
                '}';
    }
}