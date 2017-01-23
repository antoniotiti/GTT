package com.example.antonio.gestiontrabajotemporal.modelo;

import android.database.Cursor;

import com.example.antonio.gestiontrabajotemporal.sqlite.NombresColumnasBaseDatos;

public class Puesto {

    public String idPuesto;

    public String nombrePuesto;

    public String descripcionPuesto;

    public Puesto(String idPuesto, String nombrePuesto, String descripcionPuesto) {
        this.idPuesto = idPuesto;
        this.nombrePuesto = nombrePuesto;
        this.descripcionPuesto = descripcionPuesto;
    }
    public Puesto(String nombrePuesto, String descripcionPuesto) {
        this.idPuesto = null;
        this.nombrePuesto = nombrePuesto;
        this.descripcionPuesto = descripcionPuesto;
    }

    public Puesto(Cursor cursor) {
        this.idPuesto =cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Puestos.ID));
        this.nombrePuesto = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Puestos.NOMBRE));
        this.descripcionPuesto = cursor.getString(cursor.getColumnIndex(NombresColumnasBaseDatos.Puestos.DESCRIPCION));
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getNombrePuesto() {
        return nombrePuesto;
    }

    public void setNombrePuesto(String nombrePuesto) {
        this.nombrePuesto = nombrePuesto;
    }

    public String getDescripcionPuesto() {
        return descripcionPuesto;
    }

    public void setDescripcionPuesto(String descripcionPuesto) {
        this.descripcionPuesto = descripcionPuesto;
    }

    @Override
    public String toString() {
        return "Puesto{" +
                "idPuesto='" + idPuesto + '\'' +
                ", nombrePuesto='" + nombrePuesto + '\'' +
                ", descripcionPuesto='" + descripcionPuesto + '\'' +
                '}';
    }
}
