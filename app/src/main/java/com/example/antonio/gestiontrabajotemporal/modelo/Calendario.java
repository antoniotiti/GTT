package com.example.antonio.gestiontrabajotemporal.modelo;

import android.database.Cursor;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;

public class Calendario {

    public String idCalendario;
    public String nombreCalendario;
    public String descripcionCalendario;

    public Calendario(String idCalendario, String nombreCalendario, String descripcionCalendario) {
        this.idCalendario = idCalendario;
        this.nombreCalendario = nombreCalendario;
        this.descripcionCalendario=descripcionCalendario;
    }
    public Calendario( String nombreCalendario, String descripcionCalendario) {
        this.idCalendario = null;
        this.nombreCalendario = nombreCalendario;
        this.descripcionCalendario=descripcionCalendario;
    }
    public Calendario(Cursor cursor) {
        this.idCalendario =cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Calendarios.ID));
        this.nombreCalendario = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Calendarios.NOMBRE));
        this.descripcionCalendario = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Calendarios.DESCRIPCION));
    }

    public String getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(String idCalendario) {
        this.idCalendario = idCalendario;
    }

    public String getNombreCalendario() {
        return nombreCalendario;
    }

    public void setNombreCalendario(String nombreCalendario) {
        this.nombreCalendario = nombreCalendario;
    }

    public String getDescripcionCalendario() {
        return descripcionCalendario;
    }

    public void setDescripcionCalendario(String descripcionCalendario) {
        this.descripcionCalendario = descripcionCalendario;
    }

    @Override
    public String toString() {
        return "Calendario{" +
                "idCalendario='" + idCalendario + '\'' +
                ", nombreCalendario='" + nombreCalendario + '\'' +
                ", descripcionCalendario='" + descripcionCalendario + '\'' +
                '}';
    }
}
