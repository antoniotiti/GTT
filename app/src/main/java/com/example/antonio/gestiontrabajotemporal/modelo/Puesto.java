package com.example.antonio.gestiontrabajotemporal.modelo;

public class Puesto {

    public String idPuesto;

    public String nombrePuesto;

    public Puesto(String idPuesto, String nombrePuesto) {
        this.idPuesto = idPuesto;
        this.nombrePuesto = nombrePuesto;
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

    @Override
    public String toString() {
        return "Puesto{" +
                "idTurno='" + idPuesto + '\'' +
                ", nombrePuesto='" + nombrePuesto + '\'' +
                '}';
    }
}
