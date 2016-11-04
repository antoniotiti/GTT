package com.example.antonio.gestiontrabajotemporal.modelo;

public class Calendario {

    public String idCalendario;

    public String nombreCalendario;

    public Calendario(String idCalendario, String nombreCalendario) {
        this.idCalendario = idCalendario;
        this.nombreCalendario = nombreCalendario;
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

    @Override
    public String toString() {
        return "Calendario{" +
                "idCalendario=" + idCalendario +
                ", nombreCalendario='" + nombreCalendario + '\'' +
                '}';
    }
}
