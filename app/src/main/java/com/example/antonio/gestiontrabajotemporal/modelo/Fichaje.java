package com.example.antonio.gestiontrabajotemporal.modelo;


public class Fichaje {

    public String idOperario;

    public String fecha;

    public String idTurno;

    public String idPuesto;

    public String idCalendario;

    public Double horaExtra;

    public Fichaje(String idOperario, String fecha, String idTurno, String idPuesto, String idCalendario, Double horaExtra ) {
        this.idOperario = idOperario;
        this.fecha = fecha;
        this.idTurno = idTurno;
        this.idPuesto = idPuesto;
        this.idCalendario = idCalendario;
        this.horaExtra = horaExtra;
    }

    public String getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(String idOperario) {
        this.idOperario = idOperario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(String idCalendario) {
        this.idCalendario = idCalendario;
    }

    public Double getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(Double horaExtra) {
        this.horaExtra = horaExtra;
    }

    @Override
    public String toString() {
        return "Fichaje{" +
                "idOperario=" + idOperario +
                ", fecha='" + fecha + '\'' +
                ", idTurno=" + idTurno +
                ", idPuesto=" + idPuesto +
                ", idCalendario=" + idCalendario +
                ", horaExtra=" + horaExtra +
                '}';
    }
}
